How to generate Ext AS
======================

1. Download and unzip the latest Ext JS distribution

2. Download and install JSDuck

3.a Generate JSON API of old Ext JS version using JSDuck:
    cd .../ext-3.4-reference
    jsduck-x.y.z.exe --verbose --export=full --output json src

3.b Generate JSON API of lastest Ext JS version using JSDuck:
    cd .../ext-6.0.0.latest
    jsduck-x.y.z.exe --verbose --export=full --output json packages/core/src classic/classic/src

4. Clone Github CoreMedia/ext-as and checkout branch "generated"

5. Delete ext-as src/main/joo/*, run ExtAsApiGenerator, targeting the src/main/joo directory of ext-as:
   .../ext-6.0.0.latest/json .../jangaroo-ext-as/src/main/joo ext ext-as.module .../ext-3.4-reference/json

6. In IDEA, reformat code (including "Optimize imports" and "Rearrange code")

7. Commit ext-as:generated with a message specifying the ExtAsApiGenerator sha

8. Switch ext-as to branch "master" and merge branch "generated"

9. Do manual correction on branch "master"
