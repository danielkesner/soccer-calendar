# soccer-calendar

### What is this?
This service pulls information from a public API that serves data about football teams, matches, and leagues. It processes this data and uses it to compile a list of all upcoming matches where both teams are highly-ranked in their respective leagues. For example, you can use this service to generate a list of all future Premier League matches in which both teams are in the Top 6 positions of the EPL table. The goal of this service is to allow dedicated soccer fans to be notified of any exciting matches that are occurring in the future, so they can make plans to watch the game live, record it, or follow it in some other way.

### How this repository is set up
The master branch will always be even with whatever the most recent release cut is, and the version number will always be a SNAPSHOT. New functionality will gradually be merged into the dev branch as it is developed and tested. Once the dev branch has a significant feature set, it will be merged into master, and a new release branch (i.e. 0.0.2) will be cut from master.

### Releases
0.0.1 - Initial commit, minimum working code that generates all upcoming matches between top `n` Premier League teams. Less than optimal resource management (no caching, new requests are sent in multiple methods), some functional code duplication across classes, and room to improve the backend model (both in efficiency and abstraction).

### Upcoming releases in progress
0.1.0 
- Improve resource efficiency; only make external API calls when absolutely necessary. 
- Build caching system that loads all necessary data **once** on application startup. 
- Remove any duplicate functionality across classes so that abstractions are more clearly defined. 
- Ensure proper authentication to avoid rate-limiting issue currently seen. 
- Build a single externally-facing class that includes a run() method and any necessary getters/setters to allow project to be used "as a service", by simply including it in another project's POM and interacting with a single class.

0.2.0
- Integrate application with Google Calendar API to automatically populate user's calendar with generated matches.
