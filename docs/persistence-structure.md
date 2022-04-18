# Persistence Structure

## Summary
- Immutable data is like medical records, they don't change, but more information can be added to your history
- Mutable data is like whiteboard, data can be deleted or changed after it is created
- Early limitations of storage technology caused software engineers to optimize for space,
  resulting in data models that were different from the physical analog of adding files to a filling cabinet,
  leading to the reliance on mutable data being the default state
- For modern applications, our default state should be to rely on immutable data,
  that is no deletes, no updates, until we can articulate a good reason why we don't need to.

## Categories
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

Immutable databases are typically going to be event streams.
It is recording a thing, that happened in the past, at a certain time.

Mutable databases are going to have a lot more variety,
as they are tailored to how they are accessed rather than how the data gets there.
A lot of times you will have different projections or derivations of the same data, driven by different access needs.

For both immutable and mutable databases, I try to have an incremental data model.
The immutable data model tends to evolve from me adding new types of events that my system needs to support.
The mutable data model tends to evolve from how I end up accessing that data. 

For every piece of data I have,
I decide which of these two categories are most appropriate.
I do not have other categories.
I do not mix the two types of data in the same data store.
My suggestion is that when designing your persistence strategy,
explore the limits of both extremes to help you puzzle out an appropriate middle,
but make the immutable strategy your default extreme,
as making a mistake in that direction can be fixed later.

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

## Example
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

For now, I have normalized the mutable data.
For more information on normalization, look up normal forms within the context of database normalization.

I don't necessarily keep my data in a relational database, or even start it out there.
I try to incrementally maintain a normalized model of my data, as that helps me think clearly about design.
Then, I denormalize as necessary during implementation.
If I am denormalizing for performance reasons, I make sure to measure actual production performance.
I also don't worry about normalizing my event streams,
as for those the priority is to protect all original data from destructive operations such as update or delete.

## Final Thoughts
- Think about both extremes of immutable vs. mutable patterns, but remember that immutable patterns are more forgiving of the unexpected.
- Take into account the tradeoffs between how eventual consistency and ACID (atomicity, consistency, isolation, durability) meet your scalability needs.
- If you don't know, don't throw away information, have a history of how you got into the current state

## Further Reading
- Java Concurrency in Practice, Section 3.4, Immutability
- [Command Query Responsibility Segregation](https://martinfowler.com/bliki/CQRS.html)
