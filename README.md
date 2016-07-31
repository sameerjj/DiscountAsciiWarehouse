# DiscountAsciiWarehouse
This was a sample project created for a job application. 

Specs
----
The following mocks were provided:
![main screen](https://cloud.githubusercontent.com/assets/7051257/17274787/4c89a2de-56bf-11e6-9975-fbc1beef7b68.jpg)
![product](https://cloud.githubusercontent.com/assets/7051257/17274788/4f8946ba-56bf-11e6-852f-32fc7d3f7fcd.jpg)

Additionally information was provided regarding app behaviour and API specifications.

Results
----
![demo](https://cloud.githubusercontent.com/assets/7051257/17274796/878bb552-56bf-11e6-81e6-4c0b13bc83b8.gif)

Submission Questions
----
**"notes on technical decisions you made, and the pros / cons of that approach"**
- I decided to use standard material design components where appropriate in the UI. I believe this would be more familiar to users.
- I used a search button instead of just a textfield. This stops from hitting the API uneccessarily frequently, since it's not too fast to begin with. No cons really.
- I used a recylerview to implement the uneven grid UI instead of GridView. This is because it comes with a span function that made the uneven cells easily implementable. No cons really excpet this was the first time I used the RecyclerView.
- I implemented the API using OkHttp for processing requests and GSON for parsing. I decided to use this over Retrofit because the API returned NDJSON, which is not very standard for REST APIs so using the built in Retrofit Gson implementation would be useless since I would have to process the JSON manually anyways.
- I used OkHTTP source header manipulation to enable caching that's already baked in to the client. However this stops me from having more granular control over the caching and makes it difficult to test the exact caching behaviour.

**"recommendations that you would want to see followed up if this were the beginning of a long-term project?"**
- I believe that the caching behaviour should be specified in the API response headers. My research indicates this is the standard behaviour, and also makes caching behaviour configurable in between app releases.
- The API response time would have to be load tested and sped up to prevent end user frustration.
- The purchasing behaviour would have to be implemented with respect to Google Play best practices. If it's a software good, then it has to be processed using Google Play APIs.
- How could we make the layout responsive to different device sizes?
- If more APIs are to be implmented, might want to veer away from NDJSON. Parsing this on Android prevents certain optimizations from being implemented.
- search filters would be handy.
