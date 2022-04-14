# Persistence Structure

source definition of immutable
track all data you can, but practical limitations (resource limitations, pii, truth without no historical data)
CQRS - https://martinfowler.com/bliki/CQRS.html

I categorize every datum into one of two kinds
- Source of Truth
  - This is a recording of events that happened, with no processing or computations.
    All original information is here, with all other information recomputable from this.
  - canonical
      - if there is any discrepancy with other types of data, this is considered to be the authority 
  - immutable
      - in the context of computer science, immutable means only create and read operations are allowed, update and delete operations are not allowed
  - durable
      - the most protected of data, steps are taken to make sure it is never lost
  - original
      - not computed from parts or another source
- Working Copies or Projections
  - This is a view of all the events that happened up until this point.
  - not canonical
      - source of truth is elsewhere
  - mutable
      - all operations, create, read, update, delete, are allowed
  - transient
      - does not need to be preserved
  - computed
      - can be derived from original source

For every piece of data you have,
you should decide which of these two categories are most appropriate.
You should not have other categories.
You should not mix the two types of data in the same data store.
When designing your persistence strategy,
explore the limits of both extremes to help you puzzle out an appropriate middle.

At one extreme, you could go with ONLY persisting immutable data.
This means you keep all of the computed data in memory,
and have to re-derive from immutable data every time the application is restarted.
I always start at this extreme until I can demonstrate why it is not sufficient.
Don't just assume it is not a viable strategy long term,
it usually is not, but you should understand the specifics of why.
Make sure you can explain both where it will break down and at what threshold.
Thinking about what you are doing means solving the problem you actually do have, not the problem you think you might have.

At the other extreme, you could go with ONLY persisting mutable data.
This is in fact, what most inexperienced engineers will do.
Pay attention to every update and delete operation, these operations destroy data.
How certain are you that no update or delete operation will be run by mistake?
How certain are you that you will never need the destroyed information in the future?

For this application, I use both types of persistence.
The immutable data is a denormalized event log.
You can tell it is not even in first normal form because the "text" column of the "event" table has more than one piece of information in it.

event table

| id  | when                       | authority | type          | text                                                                                                                                                                          |
| --- | ---                        | ---       | ---           | ---                                                                                                                                                                           |
|   1 | 2022-03-17T20:42:00.744399 | Alice     | AddUser       | {"name":"Alice","email":"alice@email.com","salt":"6BFDFE719BE26519EAB2A88FB11AFCFA","hash":"77AD79890121131E6AAE055610942399D61468D9FB57C7CA2D0DB3DB3AC0487A","role":"OWNER"} |
|   2 | 2022-03-17T20:42:01.244457 | Bob       | AddUser       | {"name":"Bob","email":"bob@email.com","salt":"EB5B5F3DB4AFAA1B1E4A03236C8F3B6E","hash":"12923803F388CE705855C8B910924861CE11033F821ED53353CEBE08FA4F5841","role":"VOTER"}     |
|   3 | 2022-03-17T20:42:01.322288 | Alice     | SetRole       | {"name":"Bob","role":"USER"}                                                                                                                                                  |
|   4 | 2022-03-17T20:42:01.393686 | Bob       | AddElection   | {"owner":"Bob","name":"Favorite Color"}                                                                                                                                       |
|   5 | 2022-03-17T20:42:01.470504 | Bob       | AddCandidates | {"electionName":"Favorite Color","candidateNames":["Red","Green","Blue"]}                                                                                                     |

The mutable data is deriviable from the immutable data.
Each event from the event table is processed in order to create the corresponding changes to the mutable data.
The last event processed is stored in the mutable database at int_variable.last-synced

int_variable table

| id  | name        | value |
| --- | ---         | ---   |
|   1 | last-synced |     5 |

user table

| id  | name  | email           | role  | salt                             | hash                                                             |
| --- | ---   | ---             | ---   | ---                              | ---                                                              |
|   1 | Alice | alice@email.com | OWNER | 6BFDFE719BE26519EAB2A88FB11AFCFA | 77AD79890121131E6AAE055610942399D61468D9FB57C7CA2D0DB3DB3AC0487A |
|   2 | Bob   | bob@email.com   | USER  | EB5B5F3DB4AFAA1B1E4A03236C8F3B6E | 12923803F388CE705855C8B910924861CE11033F821ED53353CEBE08FA4F5841 |

election table

| id  | owner_id | name           | secret_ballot | no_voting_before | no_voting_after | allow_vote | allow_edit |
| --- | ---      | ---            | ---           | ---              | ---             | ---        | ---        |
|   1 |        2 | Favorite Color |         false |           <null> |          <null> |      false |       true |

candidate table

| id  | election_id | name  |
| --- | ---         | ---   |
|   3 |           1 | Blue  |
|   2 |           1 | Green |
|   1 |           1 | Red   |

For now, I have kept the mutable data normalized.
For more information on normalization, look up normal forms within the context of database normalization.

I don't necessarily keep my data in a relational database, or even start it out there.
I maintain a normalized model of my data, as that helps me think clearly about design.
Then, I denormalize as necessary during implementation.
If I am denormalizing for performance reasons, I only do this once I have measured actual production performance.
I also keep a denormalized view corresponding to an event log,
to make sure I retain all data from destructive operations such as update or delete.

Relational databases are good at giving you decent overall performance for when you don't know how data will be accessed,
or when data will be accessed in multiple ways that can't be optimized together.
Key-value stores or graph databases are better for when you want to optimize for specific data access patterns at the expense of others.
Triple stores give you a mix of the capabilities of both.
