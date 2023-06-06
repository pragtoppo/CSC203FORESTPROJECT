import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy {

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {

            PriorityQueue<Node> openList = new PriorityQueue<>();
            Map<Point, Node> closedList = new HashMap<>();
            Node startNode = new Node(start, null, 0, heuristic(start, end));
            openList.add(startNode);
            //closedList.put(start, startNode);
            while (!openList.isEmpty()) {
                Node current = openList.poll();
                Point currentPoint = current.point;
                System.out.println("Current Node: " + current.point);

                if (withinReach.test(currentPoint, end)) {
                    System.out.println("Within reach of the goal, skipping neighbors");
                    return reconstructPath(current);
                }
                System.out.println("add to closed list");
                closedList.put(current.point, current);
//           // for (Point neighbor : potentialNeighbors.apply(current.point).collect(Collectors.toList())) {
//                if (canPassThrough.test(neighbor)) {
////                    int tGscore = current.gscore + 1;
////                    Node neighborNode = closedList.get(neighbor); //neighborNode is our existing node and our neighbours are the valid points
//                    boolean isnewNode = false;
//                    if (neighborNode == null) {
//                        neighborNode = new Node(neighbor, current, tGscore, heuristic(neighbor, end));
//                        //openList.add(neighborNode);
//                        isnewNode = true;
//                    } else if (tGscore < neighborNode.gscore) {
//                        neighborNode.gscore = tGscore;
//                        neighborNode.fscore = neighborNode.gscore + heuristic(neighbor, end);
//                        neighborNode.prior = current;
//                        System.out.println("the neighbour becomes the current node ");
//                    }
//                    if (isnewNode) {
//                        System.out.println("Add neighbour node to the open list");
//                        openList.add(neighborNode);
//                    }
//                }
                potentialNeighbors.apply(currentPoint).filter(canPassThrough).forEach(neighbor -> {
                    int tgscore = current.gscore + 1;
                    int fscore = tgscore + heuristic(neighbor, end);
                    Node neighborNode = closedList.get(neighbor);
                    //System.out.println(neighborNode.point);
                    if (neighborNode != null && tgscore >= neighborNode.gscore) {
                        return;
                    }
                    neighborNode = openList.stream().filter(node -> node.point.equals(neighbor)).findFirst().orElse(null);
                    if (neighborNode != null && tgscore >= neighborNode.gscore) {
                        return;
                    }
                    neighborNode = new Node(neighbor, current, tgscore, fscore);
                    if (neighborNode != null) {
                        openList.remove(neighborNode);
                    }
                    openList.add(neighborNode);
                });
            }
            System.out.println("No path found!");
            return Collections.emptyList();
//        List<Point> path = new LinkedList<Point>();
            //return path;
        }
//    private List<Point> reconstructPath(Map<Point, Node> closed)
//    {
//        List<Point> path = new ArrayList<>();
//        Deque<Node> stack = new ArrayDeque<>();
//        System.out.println("L");
//        for(Map.Entry<Point, Node> points: closed.entrySet())
//        {
//            Node current = points.getValue();
//            stack.push(current);
//            //current = current.parent;
//        }
//        while (!stack.isEmpty()) {
//            Node current = stack.pop();
//            path.add(current.point);
//        }
//        System.out.println("path returned");
//        return path;
//    }

        private List<Point> reconstructPath (Node node)
        {
            System.out.println("L");
            List<Point> path = new ArrayList<>();
            Node current = node;
            while (current.prior != null) {
                path.add(0, current.point);
                current = current.prior;
            }
            //Collections.reverse(path);
            System.out.println("path returned");
            return path;
        }
    private int heuristic(Point p1, Point p2)
    {
        return Math.abs(p1.x -p2.x) + Math.abs(p1.y - p2.y);
    }
    }
//
//            PriorityQueue<Node> openList = new PriorityQueue<>();
//            Map<Point, Node> closedList = new HashMap<>();
//            Node startNode = new Node(start, null, 0, heuristic(start, end));
//            openList.add(startNode);
//            //closedList.put(start, startNode);
//            while (!openList.isEmpty()) {
//                Node current = openList.poll();
//                System.out.println("Current Node: " + current.point);
//                if (withinReach.test(current.point, end)) {
//                    System.out.println("Within reach of the goal, skipping neighbors");
//                    continue;
//                }
//                closedList.put(current.point, current);
//                if (current.point.equals(end)) { //if the neighbour node is the end then the just send current node through the path
//                    return reconstructPath(current);
//                }
//                for (Point neighbor : potentialNeighbors.apply(current.point).toList()) {
//                    if (!canPassThrough.test(neighbor)) {
//                        continue;
//                    }
//                    int tGscore = current.gscore + 1;
//                    Node neighborNode = closedList.get(neighbor);
//                    boolean isnewNode = false;
//                    if (neighborNode == null) {
//                        neighborNode = new Node(neighbor, current, tGscore, heuristic(neighbor, end));
//                        closedList.put(neighbor, neighborNode);
//                        isnewNode = true;
//                    } else if (tGscore < neighborNode.gscore) {
//                        neighborNode.gscore = tGscore;
//                        neighborNode.fscore = neighborNode.gscore + heuristic(neighbor, end);
//                        neighborNode.parent = current;
//                        System.out.println("the neighbour becomes the current node ");
//                    }
//                    if (isnewNode) {
//                        openList.add(neighborNode);
//                    }
//                }
//            }
//            System.out.println("No path found!");
//            return Collections.emptyList();
////        List<Point> path = new LinkedList<Point>();
//            //return path;
//        }
//        private List<Point> reconstructPath (Node node)
//        {
//            List<Point> path = new ArrayList<>();
//            Node current = node;
//            while (current != null) {
//                path.add(0, current.point);
//                current = current.parent;
//            }
//            return path;
//        }
//        private int heuristic (Point p1, Point p2)
//        {
//            return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
//        }


