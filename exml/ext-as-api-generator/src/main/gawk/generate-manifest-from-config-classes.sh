#!/bin/sh
echo "generating component package of config classes in $1 into '$2'"
echo "<?xml version=\"1.0\"?>" > $2
echo "<componentPackage>" >> $2
ls $1/*.as | xargs -n 1 cat | gawk '/\[ExtConfig/ { match($1, /"[^"]*"/); classname = substr($0, RSTART+1, RLENGTH-2); } /public dynamic class/ { print "  <component id=\""$4"\" class=\""classname"\"/>"; }' >> $2
echo "</componentPackage>" >> $2
