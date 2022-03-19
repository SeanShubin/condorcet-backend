# Style Guide
- [Object Relational Mapping](object-relational-mapping.md)
- [Persistence Structure](persistence-structure.md)
- [Effective Logging](effective-logging.md)
- [Authentication and Authorization](authentication-and-authorization.md)
- [Self Healing Systems](self-healing-systems.md)


## Two kinds of data

### Source of truth

This is a recording of events that happened, with no processing or computations.
All original information is here, with all other information recomputable from this.

### Working copy

This is a current view of all the events that happened up until this point.
