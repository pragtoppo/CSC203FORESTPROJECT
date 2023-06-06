public class Node implements Comparable<Node>{
    Point point;
    Node prior;
    int gscore;
    int fscore;
    Node(Point point, Node prior, int gscore, int fscore)
    {
        this.point =point;
        this.prior = prior;
        this.gscore = gscore;
        this.fscore = fscore;
    }
    @Override
    public int compareTo(Node other)
    {
//        int fscore = gscore + hscore;
//        int otherFscore = other.gscore + other.hscore;
        return Integer.compare(fscore, other.fscore);
    }
}
