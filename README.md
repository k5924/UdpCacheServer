# UDPCacheServer

This is a UDP Cache Server built in Java.

## Functionality:
### Client Operations
- GET a value from a key in the cache
- PUT a key value pair in the cache
- DELETE a key value pair from the cache
- HEARTBEAT to check if the server is alive

These operations can be conducted from the clients CLI.

## Architecture
### Client
- Client has a Sender and Receiver Thread so you can always receive responses
### Server
- The server has a main thread which spawns threads up to N CPUs (available to the system) to handle N number of clients at a time. On my machine, I have 8 cores so my machine can handle 8 connections at  a time.

## Improvements
- Need to add tests as nothing has any tests around them
- Could improve logic in Cache as its very simple right now (just uses a ConcurrentHashMap so there are probably tons of race conditions in there)
- Use a buffering mechanism to batch up requests to handle on the server