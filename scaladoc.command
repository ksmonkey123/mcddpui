cd $(dirname "$0")
/usr/local/bin/scaladoc -diagrams -d docs $(find . -name *.scala)
open docs/index.html
