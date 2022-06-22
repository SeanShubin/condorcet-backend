# Object Relational Mapping

Typical orm dependency structure

![](typical-orm.svg)

My orm dependency structure

![](my-orm.svg)

I never let my service layer become aware of database keys. The only reason I used database keys is at the database
layer to perform joins or lookups. My intention is to protect the abstraction boundary between the service and the
database.

service module

```kotlin
data class RefreshToken(val userName: String)
data class AccessToken(val userName: String, val role: Role)
data class Tokens(val refreshToken: RefreshToken, val accessToken: AccessToken)
data class ElectionDetail(
    val ownerName: String,
    val electionName: String,
    val candidateCount: Int,
    val voterCount:Int,
    val secretBallot: Boolean = true,
    val noVotingBefore: Instant? = null,
    val noVotingAfter: Instant? = null,
    val allowEdit: Boolean = true,
    val allowVote: Boolean = false,
)
interface Service {
    fun register(userName: String, email: String, password: String): Tokens
    fun setRole(accessToken: AccessToken, userName: String, role: Role)
    fun addElection(accessToken: AccessToken, userName:String, electionName: String)
    fun getElection(accessToken: AccessToken, electionName: String): ElectionDetail
    fun setCandidates(accessToken: AccessToken, electionName: String, candidateNames: List<String>)
}
```

The service delegates to the database to modify the database

Implementation of addElection, in the service module

```kotlin
override fun addElection(accessToken: AccessToken, userName:String, electionName: String) {
    requirePermission(accessToken, USE_APPLICATION)
    val validElectionName = validateElectionName(electionName)
    requireElectionNameDoesNotExist(electionName)
    mutableDbCommands.addElection(accessToken.userName, userName, validElectionName)
}
```

The contract of the database does not even expose database ids. Also, the service is not a 1:1 pass-through to the
database. The service often calls many database commands, or composes many database queries.

database module

```kotlin
interface MutableDbCommands {
    fun createUser(
        authority: String,
        userName: String,
        email: String,
        salt: String,
        hash: String,
        role: Role
    )
    fun setRole(authority: String, userName: String, role: Role)
    fun addElection(authority: String, owner: String, electionName: String)
    fun addCandidates(authority: String, electionName: String, candidateNames: List<String>)
    fun removeCandidates(authority: String, electionName: String, candidateNames: List<String>)
}
```

If we follow addElection down to the database implementation, we see all we are doing in kotlin code is choosing the
name of the query, and the order of the parameters. We still have not exposed the fact that we are using a relational
database or sql.

database module

```kotlin
class MutableDbCommandsImpl(
    genericDatabase: GenericDatabase
) : MutableDbCommands, GenericDatabase by genericDatabase {
    override fun addElection(authority: String, owner: String, electionName: String) {
        update("election-insert", owner, electionName)
    }
}
```

We finally get to library that handles object relational mapping. It is at this point that we start talking to JDBC.

genericdb (orm module)

```kotlin
class QueryLoaderFromResource : QueryLoader {
  override fun load(name: String): String {
    val resourceName = "com/seanshubin/condorcet/backend/database/$name.sql"
    val charset = StandardCharsets.UTF_8
    val classLoader = this.javaClass.classLoader
    val inputStream = classLoader.getResourceAsStream(resourceName)
    if (inputStream == null) {
      throw RuntimeException("Resource named '$resourceName' not found")
    } else {
      return inputStream.consumeString(charset)
    }
  }
}
class ConnectionWrapper(
  private val connection: Connection,
  private val sqlEvent: (String) -> Unit,
  private val sqlException: (String, String, SQLException) -> Unit
) : AutoCloseable {
  fun update(name: String, code: String, vararg parameters: Any?): Int {
    val statement = connection.prepareStatement(code) as ClientPreparedStatement
    updateParameters(name, parameters, statement)
    return statement.use {
      executeUpdate(name, statement)
    }
  }

  private fun updateParameters(name: String, parameters: Array<out Any?>, statement: ClientPreparedStatement) {
    try {
      parameters.toList().forEachIndexed { index, any ->
        val position = index + 1
        if (any == null) {
          statement.setObject(position, null)
        } else when (any) {
          is String -> statement.setString(position, any)
          is Boolean -> statement.setBoolean(position, any)
          is Int -> statement.setInt(position, any)
          is Instant -> statement.setTimestamp(position, Timestamp.from(any))
          else -> throw UnsupportedOperationException("Unsupported type ${any.javaClass.simpleName}")
        }
      }
    } catch (ex: SQLException) {
      throw SQLException("$name\n${statement.asSql()}\n${ex.message}", ex)
    }
  }

  private fun executeUpdate(name: String, statement: PreparedStatement): Int {
    try {
      sqlEvent(statement.asSql())
      return statement.executeUpdate()
    } catch (ex: SQLException) {
      sqlException(name, statement.asSql(), ex)
      throw SQLException("$name\n${statement.asSql()}\n${ex.message}", ex)
    }
  }

  private fun Statement.asSql(): String = (this as ClientPreparedStatement).asSql()
}
class GenericDatabaseImpl(
    private val connection: ConnectionWrapper,
    private val queryLoader: QueryLoader
) : GenericDatabase {
    override fun update(name: String, vararg parameters: Any?): Int {
        val code = queryLoader.load(name)
        return connection.update(name, code, *parameters)
    }
}
```

Finally, the sql statements are not sitting in kotlin files, they are in .sql files. This allows us to connect our
Integrated Development Environment to the database, so we have autocomplete and syntax highlighting while we write our
queries, as well as being warned when we mistype a column name.

election-insert.sql

```sql
insert into election (owner_id, name)
values ((select id from user where name = ?), ?)
```

All of our sql statements can be found in
the [database](../database/src/main/resources/com/seanshubin/condorcet/backend/database/) module Since our sql is
isolated, we can write an integration test for each one. You can usually get away with only a single integration test
per query. It is a matter of setting up the data such that each bit of conditional logic can be tested by the existence
or nonexistence of a row.

The genericdb (orm module), that only has to be written once, can be tested by stubbing out resultsets.

The database module, which is aware of the domain, can be fully unit tested without any knowledge of JDBC.

Now let's have a look at queries

The service delegates to the database to query the database.

Implementation of getElection, in the service module

```kotlin
override fun getElection(accessToken: AccessToken, electionName: String): ElectionDetail {
    requirePermission(accessToken, VIEW_APPLICATION)
    val election = findElection(electionName)
    val candidateCount = mutableDbQueries.candidateCount(electionName)
    val voterCount = mutableDbQueries.voterCount(electionName)
    val electionDetail = election.toElectionDetail(candidateCount, voterCount)
    return electionDetail
}
```

Notice that the service executes 3 queries. I could have also made a single query for this api call, as I am not locked
in to a 1:1 mapping between service calls and database calls.

database module

```kotlin
interface MutableDbQueries : GenericDatabase {
    fun searchElectionByName(name: String): ElectionSummary?
    fun candidateCount(electionName:String):Int
    fun voterCount(electionName:String):Int
}
```

If we follow searchElectionByName down to the database implementation, we see all we are doing in kotlin code is
choosing the name of the query, and the order of the parameters. Notice that in candidateCount we use the election name
rather than the election id, so still have not exposed the fact that we are using a relational database or sql.

For queries, I write a mapping function from a ResultSet to a domain object. These functions usually only look at the
current row of the ResultSet, and also handle any needed type conversions, such as from a java.sql.Timestamp to an
java.time.Instant

database module

```kotlin
class MutableDbQueriesImpl(genericDatabase: GenericDatabase) : MutableDbQueries,
    GenericDatabase by genericDatabase {

    override fun searchElectionByName(name: String): ElectionSummary? =
        queryZeroOrOneRow(::createElectionSummary, "election-select-by-name", name)

    override fun electionCount(): Int =
        queryExactlyOneInt("election-count")

    override fun candidateCount(electionName: String): Int =
        queryExactlyOneInt("candidate-count-by-election", electionName)

    private fun createElectionSummary(resultSet: ResultSet): ElectionSummary {
        val owner = resultSet.getString("owner")
        val name: String = resultSet.getString("name")
        val secretBallot: Boolean = resultSet.getBoolean("secret_ballot")
        val noVotingBefore: Instant? = resultSet.getTimestamp("no_voting_before")?.toInstant()
        val noVotingAfter: Instant? = resultSet.getTimestamp("no_voting_after")?.toInstant()
        val allowEdit: Boolean = resultSet.getBoolean("allow_edit")
        val allowVote: Boolean = resultSet.getBoolean("allow_vote")
        return ElectionSummary(
            owner, name, secretBallot, noVotingBefore, noVotingAfter, allowEdit, allowVote
        )
    }
}
```

Here are the corresponding sql queries, in separate files. My IDE connects .sql files to the database schema, so I have
autocomplete, syntax highlighting, and I get visual indicators when I type a name wrong.

election-select-by-name.sql

```sql
select user.name as owner,
       election.name,
       election.secret_ballot,
       election.no_voting_before,
       election.no_voting_after,
       election.allow_edit,
       election.allow_vote
from election
         inner join user on election.owner_id = user.id
where election.name = ?
```

election-count.sql

```sql
select count(id)
from election
```

candidate-count-by-election.sql

```sql
select
       count(candidate.id)
from candidate
    inner join election
        on candidate.election_id = election.id
where
      election.name = ?
```

By the time we start talking to JDBC, the data types are generic, so from here we are not coupled to the domain.

genericdb (orm module)

```kotlin
class ConnectionWrapper(
  private val connection: Connection,
  private val sqlEvent: (String) -> Unit,
  private val sqlException: (String, String, SQLException) -> Unit
) : AutoCloseable {
    fun <T> queryZeroOrOneRow(name: String, code: String, vararg parameters: Any?, f: (ResultSet) -> T): T? =
        query(name, code, *parameters) { resultSet ->
            if (resultSet.next()) {
                val result = f(resultSet)
                if (resultSet.next()) {
                    throw RuntimeException("No more than 1 row expected for '$name'\n$code")
                }
                result
            } else {
                null
            }
        }
}
class GenericDatabaseImpl(
    private val connection: ConnectionWrapper,
    private val queryLoader: QueryLoader
) : GenericDatabase {

    override fun <T> queryZeroOrOneRow(
        createFunction: (ResultSet) -> T,
        name: String,
        vararg parameters: Any?
    ): T? {
        val code = queryLoader.load(name)
        return connection.queryZeroOrOneRow(name, code, *parameters) { createFunction(it) }
    }

    override fun queryExactlyOneInt(name: String, vararg parameters: Any?): Int =
        queryExactlyOneRow(::createInt, name, *parameters)

    private fun createInt(resultSet: ResultSet): Int = resultSet.getInt(1)
}
```

Here are sql statements backing the getElection feature

election-insert.sql

```sql
insert into election (owner_id, name)
values ((select id from user where name = ?), ?)
```

For more complicated structures, I use generic data types to compose the strucure, and creator functions for the
specific instances.

Here is generic code for handling a parent/child structure. For each row, I grab the parent and the child, group by the
key, and then compose a parent with a list of children.

```kotlin
override fun <ParentType, ChildType, ParentKeyType, ResultType> queryParentChild(
    parentFunction: (ResultSet) -> ParentType,
    childFunction: (ResultSet) -> ChildType,
    parentKeyFunction: (ResultSet) -> ParentKeyType,
    composeFunction: (ParentType, List<ChildType>) -> ResultType,
    name: String,
    vararg parameters: Any?
): List<ResultType> {
    val code = queryLoader.load(name)

    data class Row(val parent: ParentType, val child: ChildType, val key: ParentKeyType)

    val allRows = connection.queryList(name, code, *parameters) {
        Row(parentFunction(it), childFunction(it), parentKeyFunction(it))
    }
    val grouped = allRows.groupBy { it.key }
    val results = grouped.map { (key, rows) ->
        val parent = rows[0].parent
        val children = rows.map { it.child }
        composeFunction(parent, children)
    }
    return results
}
```

And here is the invocation, along with the creator functions.

```kotlin
override fun listBallots(electionName: String): List<RevealedBallot> =
    queryParentChild(
        ::createBallotSummary,
        ::createRanking,
        ::createBallotRankingKey,
        ::createBallot,
        "ranking-select-by-election",
        electionName
    )

private fun createBallotSummary(resultSet: ResultSet): BallotSummary =
    BallotSummary(
        resultSet.getString("user"),
        resultSet.getString("election"),
        resultSet.getString("confirmation"),
        resultSet.getTimestamp("when_cast").toInstant()
    )

private fun createRanking(resultSet: ResultSet): Ranking =
    Ranking(
        resultSet.getString("candidate"),
        resultSet.getInt("rank")
    )

private fun createBallotKey(resultSet: ResultSet): Int =
    resultSet.getInt("ballot.id")

private fun attachRankingsToBallot(ballotSummary: BallotSummary, rankingList: List<Ranking>): RevealedBallot =
    RevealedBallot(
        ballotSummary.voterName,
        ballotSummary.electionName,
        ballotSummary.confirmation,
        ballotSummary.whenCast,
        rankingList.map { Ranking(it.candidateName, it.rank) })
```

What about query caching?

A lot of times you can eliminate the need for this by arranging your code so that you are not making repetitive calls to
the database. If you know you are likely to need the data soon, keep it in memory. This is harder to do if your queries
are dynamically generated by something you don't control. I value libraries that solve problems I would have had without
them. It is not impressive for a library to solve a problem created by that library. I would rather not have the problem
in the first place.

What about being able to change the underlying database as in other ORM strategies?

While that is often touted as a primary advantage of some ORM tools, I have never seen or heard of that being done, not
once, so I suspect the need for this somewhere between non-existent and rare. If we are considering changing to another
RELATIONAL database, the only changes entail updating the keyword and reserved word lists, and minor changes to the SQL
queries, as SQL does not actually vary that much between relational databases, and I only use the features of the SQL
dialect that I turn out to actually need. If we are considering changing to something other than a relational database,
this architecture actually makes that easier because I did not couple the domain to the database. Primary or foreign
keys are not exposed through the service layer or the interfaces in the database layer. From the architecture, we can't
tell we are dealing with a relational database until we get to the implementations of the database layer, and even then,
most of the code with knowledge of the underling relational database is in the orm library.

So why do I reject typical orm design in favor of this one?

![](typical-orm.svg) ![](my-orm.svg)

The main problem I have with typical orm designs is where they draw the abstraction boundary. The domain layer should
not cater to the database layer, the database layer should be adapted to suit the domain layer. This does mean the
service layer has to deal with the database layer instead of the domain layer having to deal with the database layer,
but the service layer is glue, it's the service layers job to connect things together. The domain layer should be
focused on modeling the domain.
