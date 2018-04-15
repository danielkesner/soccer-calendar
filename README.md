# soccer-calendar

### What is this?
This service pulls information from a public API that serves data about football teams, matches, and leagues. It processes this data and uses it to compile a list of all upcoming matches where both teams are highly-ranked in their respective leagues. For example, you can use this service to generate a list of all future Premier League matches in which both teams are ranked in the Top 4 positions of the Premier League. The goal of this application is to notify soccer fans of any competitive matches that are occurring in the future, so they can make plans to watch the game live, record it, or follow it in some other way. You can learn more about the football-data API I used to build this application [here](http://www.football-data.org/documentation).

### How this repository is organized
The master branch will always be even with the most recent release version, and the version number will always be a SNAPSHOT. New functionality will gradually be merged into the dev branch as it is developed and tested locally. Once the dev branch has a significant feature set, it will be merged into master, and a new release branch (i.e. 1.0.0) will be cut from master.

### Releases
0.1.0
- Initial commit, minimum working code that generates all upcoming matches between top `n` Premier League teams (not generalized to multiple leagues)

0.9.0
- Rewrote main algorithm to cut down on number of loops and external API calls (runtime: <30 seconds for all European leagues excluding Champions League) and generalized to include all major European competitions
- Improved resource efficiency by building caching system that loads all necessary data **once** on application startup
- Removed duplicate functionality across classes so that abstractions are more clearly defined
- Ensured proper authentication to avoid rate-limiting by external API
- Built a single externally-facing class that includes a run() method to allow project to be used "as a service", by simply including it in another project's POM and interacting with a single class

### TODO: Upcoming releases 
1.0.0
- Integrate application with Google Calendar API to automatically populate user's calendar with generated matches.
- Add more competitions: Champions League, Europa League, World Cup, Major League Soccer (USA)
- Add basic web service functionality with Spring Boot - accept external requests and return corresponding data
