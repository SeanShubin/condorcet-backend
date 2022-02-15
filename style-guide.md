# Style Guide

## Two kinds of data

### Source of truth

This is a recording of events that happened, with no processing or computations.
All original information is here, with all other information recomputable from this.

### Working copy

This is a current view of all the events that happened up until this point.

## Configuration
Configuration is stored in json files, 
computed at the last possible moment,
and recomputed every time it is needed.

This is implemented with lookup functions, so instead passing around a value such as `port:Int`,
you pass around a function to lookup the value, such as `lookupPort:()->Int`

All lookup functions are created with a default value and the json path to that value.
For for example, say you have a configuration function factory pointing to the file "configuration.json".
From that you get a function to lookup the database port `createIntLookup(3306, listOf("database", "port"))`.
When looking up the database port for the first time, it would create the file "configuration.json" with the contents
```json
{
  "database" : {
    "port" : 3306
  }
}
```
Every future time the function is called, it returns the value already configured at that path.
In this manner, the structure of the configuration file manifests at runtime as the configuration values are needed.
If there is no sensible default value, a string describing what to do can be set as the default value instead, even if string is the wrong type.
That way the administrator can respond to application error messages by setting the appropriate values.
For example, if you set the default value like this `createIntLookup("database-port-goes-here", listOf("database", "port"))`,
the first time you run the application you get 
```json
{
  "database" : {
    "port" : "database-port-goes-here"
  }
}
```
which makes it obvious to the administrator what configuration is needed.
