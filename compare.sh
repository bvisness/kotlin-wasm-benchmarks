#!/usr/bin/env nu

def main [f1: string, ...fs: string] {
  let first = (open $f1
    | select benchmark primaryMetric.scoreUnit primaryMetric.score primaryMetric.scoreError
    | rename benchmark unit "1-score" "1-scoreError"
  )
  let all = ($fs | enumerate | reduce -f $first { |it, acc|
    let $i = $it.index + 2
    $acc | merge (open $it.item
      | select primaryMetric.score primaryMetric.scoreError
      | rename $"($i)-score" $"($i)-scoreError"
    )
  })
  $all | save benchmarks.csv
}
