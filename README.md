# Tests for the ToDo app.

<h3>Tests</h3>
There are three types of tests:
- **Smoke** test verifies that the endpoints are working
- **Contract** tests verify the parameters used in the endpoints and there types/mandatoriness
- **Tests of logic** verify that the endpoints do what they are supposed to do from the business logic point of view

<h3>POST endpoint performance measurements</h3>
_Single call_: Latency ~ **40ms**  
_100 parallel calls_: Latency from **100ms** to **124ms** (average - **103ms**)  
_1000 parallel calls_: Latency from **92ms** to **1208ms** (average - **371ms**)  
_3000 parallel calls_: Latency from **136ms** to **1614ms** (average - **603ms**)  
_6000 parallel calls_ were too much for my machine :)
