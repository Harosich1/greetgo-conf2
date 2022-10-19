package util

class GitUtil {

  static def extractLastGitCommitId(String gitFolder) {

    def takeFromHash = 17

    //noinspection GroovyAssignabilityCheck
    if (!new File(gitFolder + "HEAD").exists()) {
      return "UNKNOWN"
    }

    /*
     * '.git/HEAD' contains either
     *      in case of detached head: the currently checked out commit hash
     *      otherwise: a reference to a file containing the current commit hash
     */
    //noinspection GroovyAssignabilityCheck
    def head = new File(gitFolder + "HEAD").text.split(":") // .git/HEAD
    def isCommit = head.length == 1
    // def isRef = head.length > 1     // ref: refs/heads/master

    if (isCommit) {
      return head[0].trim().take(takeFromHash)
    }

    def reference = head[1].trim()

    //noinspection GroovyAssignabilityCheck
    def refHead = new File(gitFolder + reference) // .git/refs/heads/master
    if (refHead.exists()) {
      return refHead.text.trim().take(takeFromHash)
    }

    //noinspection GroovyAssignabilityCheck
    def packedRefsFile = new File(gitFolder + "packed-refs")

    if (packedRefsFile.exists()) {

      def lines = packedRefsFile.text.split("\n")

      for (String line : lines) {

        if (line.trim().startsWith("#")) {
          continue
        }

        def split = line.split("\\s+")
        if (split.length == 2) {

          if (split[1] == reference) {
            return split[0]
          }

        }
      }

    }

    return "no-commit"
  }


}
