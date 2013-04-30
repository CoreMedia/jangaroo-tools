How to generate Ext AS
======================

1. Download and unzip the latest Ext JS distribution

2. Download and install JSDuck (Ruby!)

3. Generate JSON API using JSDuck

4. Clone Github CoreMedia/ext-as and checkout branch "generated"

5. Delete ext-as src/main/joo/*, run ExtAsApiGenerator, targeting the src/main/joo directory of ext-as

6. In IDEA, reformat code (including "optimize imports")

7. Commit ext-as:generated with a message specifying the ExtAsApiGenerator sha

8. Switch ext-as to branch "master" and merge branch "generated"

9. Do manual correction on branch "master"
