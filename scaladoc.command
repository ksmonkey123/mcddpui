cd $(dirname "$0")
/usr/local/bin/scaladoc -implicits -implicits-show-all -diagrams -diagrams-max-implicits 10 -d docs $(find . -name *.scala)
open docs/index.html
