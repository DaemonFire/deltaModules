import java.util.ArrayList;

public class TimePartition {

    private int allTimeStartInstant;
    private int allTimeEndInstant;
    private int startInstant;
    private int endInstant;
    private TimePartition partBefore;
    private TimePartition partAfter;
    private int depth;
    private DeltaModule splitters;

    public TimePartition(int startInstant, int endInstant, int allTimeStartInstant, int allTimeEndInstant) {
        this.startInstant = startInstant;
        this.endInstant = endInstant;
        this.allTimeStartInstant = allTimeStartInstant;
        this.allTimeEndInstant = allTimeEndInstant;
        this.depth = 1;
        this.splitters = new DeltaModule(this.startInstant, this.endInstant);
    }

    public int getAllTimeStartInstant() {
        return this.allTimeStartInstant;
    }

    public int getAllTimeEndInstant() {
        return this.allTimeEndInstant;
    }

    public int getStartInstant() {
        return this.startInstant;
    }

    public int getEndInstant() {
        return this.endInstant;
    }

    public TimePartition getPartBefore() {
        return this.partBefore;
    }

    public TimePartition getPartAfter() {
        return this.partAfter;
    }

    public int getDepth() {
        return this.depth;
    }

    public DeltaModule getSplitters() {
        return splitters;
    }

    public void setAllTimeStartInstant(int allTimeStartInstant) {
        this.allTimeStartInstant = allTimeStartInstant;
        if (this.startInstant < allTimeStartInstant) {
            this.setStartInstant(allTimeStartInstant);
        }
    }

    public void setAllTimeEndInstant(int allTimeEndInstant) {
        this.allTimeEndInstant = allTimeEndInstant;
        if (this.endInstant > this.allTimeEndInstant) {
            this.setEndInstant(allTimeEndInstant);
        }
    }

    public void setStartInstant(int startInstant) {
        this.startInstant = startInstant;
        this.splitters.setStartInstant(startInstant);
    }

    public void setEndInstant(int endInstant) {
        this.endInstant = endInstant;
        this.splitters.setEndInstant(endInstant);
    }

    public void setPartBefore(TimePartition partBefore) {
        this.partBefore = partBefore;
    }

    public void setPartAfter(TimePartition partAfter) {
        this.partAfter = partAfter;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void addSplitter(Vertex u) {
        if (!splitters.getVertices().contains(u)) {
            splitters.addVertex(u);
        }
    }

    public boolean consolidateLeft(TimePartition rootPart){
        if (this.getEndInstant() == rootPart.getStartInstant() - 1){
            if (rootPart.getSplitters().isSame(this.getSplitters())){
                rootPart.setStartInstant(this.startInstant);
                return true;
            }
        }
        else {
            if (this.partAfter== null) {
                return false;
            }
            else {
                if(this.partAfter.consolidateLeft(rootPart)) {
                    TimePartition tmp = this.partAfter.partBefore;
                    this.setPartAfter(tmp);
                    this.setDepth(Math.max(this.getPartBefore().getDepth(), this.getPartAfter().getDepth()) + 1);
                }
            }
        }
        return false;
    }

    public boolean consolidateRight(TimePartition rootPart){
        if (this.getStartInstant() == rootPart.getEndInstant() + 1){
            if (rootPart.getSplitters().isSame(this.getSplitters())){
                rootPart.setEndInstant(this.endInstant);
                return true;
            }
        }
        else {
            if (this.partBefore== null) {
                return false;
            }
            else {
                if(this.partBefore.consolidateRight(rootPart)) {
                    TimePartition tmp = this.partBefore.partAfter;
                    this.setPartBefore(tmp);
                    this.setDepth(Math.max(this.getPartBefore().getDepth(), this.getPartAfter().getDepth()) + 1);
                }
            }
        }
        return false;
    }

    public void takeItDeeperRight(TimePartition part) {
        if (this.partAfter != null) {
            this.partAfter.takeItDeeperRight(part);
            this.setDepth(Math.max(this.partAfter.getDepth() + 1, this.getDepth()));
        }
        else {
            this.setPartAfter(part);
            this.setDepth(Math.max(2, this.getDepth()));
        }
        balanceThatTree();
    }

    public void takeItDeeperLeft(TimePartition part) {
        if (this.partBefore != null) {
            this.partBefore.takeItDeeperLeft(part);
            this.setDepth(Math.max(this.partBefore.getDepth() + 1, this.getDepth()));
        }
        else {
            this.setPartBefore(part);
            this.setDepth(Math.max(2, this.getDepth()));
        }
        balanceThatTree();
    }

    public void addSplitter(Vertex u, int t) {
        if (t < this.startInstant) {
            if (this.partBefore == null) {
                this.partBefore = new TimePartition(t, t, this.allTimeStartInstant, this.startInstant - 1);
                this.partBefore.addSplitter(u);
                this.depth = Math.max(2, this.depth);
            } else {
            //    System.out.println("Depth: "+this.getDepth());
                this.partBefore.addSplitter(u, t);
                this.setDepth(Math.max(this.depth, this.getPartBefore().getDepth() + 1));
                if (t==this.startInstant-1){
                    this.consolidateLeft(this);
                }
            }

        } else {
            if (t < this.endInstant) {
                if (!splitters.getVertices().contains(u)) {
                    TimePartition newPartBefore = new TimePartition(startInstant, t - 1, allTimeStartInstant, t - 1);
                    TimePartition newPartAfter = new TimePartition(t + 1, endInstant, t + 1, allTimeEndInstant);
                    for (Vertex v : splitters.getVertices()) {
                        newPartBefore.addSplitter(v);
                        newPartAfter.addSplitter(v);
                    }
                    this.partBefore.takeItDeeperRight(newPartBefore);
                    this.partAfter.takeItDeeperLeft(newPartAfter);
                    this.setDepth(Math.max(this.getPartAfter().getDepth(), this.getPartBefore().getDepth()) +1);
                    this.addSplitter(u);
                    this.setStartInstant(t);
                    this.setEndInstant(t);
                }
            } else {
                if (this.partAfter == null) {
                    this.partAfter = new TimePartition(t, t, this.endInstant + 1, this.allTimeEndInstant);
                    this.partAfter.addSplitter(u);
                    this.setDepth(Math.max(this.getDepth(), 2));
                } else {
                    this.partAfter.addSplitter(u, t);
                    this.setDepth(Math.max(this.depth, this.partAfter.depth + 1));
                    if (t==this.endInstant+1) {
                        this.consolidateRight(this);
                    }
                }
            }
        }
        balanceThatTree();

    }

    public void removeInstant(int t, int delta) {

        if (t == startInstant - 1) {
            this.startInstant = t;
            if (this.partBefore != null) {
                this.partBefore.recursivelyChangeEndInstant(t);
            }
        }

        if (t == endInstant + 1) {
            this.endInstant = t;
            if (this.partAfter != null) {
                this.partAfter.recursivelyChangeStartInstant(t);
            }
        }

        if (t < startInstant - 1) {
            if (this.partBefore == null) {
                partBefore = new TimePartition(t, t, allTimeStartInstant, startInstant - 1);
            } else {
                partBefore.removeInstant(t, delta);
            }
            this.setDepth(Math.max(this.getDepth(), this.partBefore.getDepth() + 1));
        }

        if (t > endInstant + 1) {
            if (this.partAfter == null) {
                partAfter = new TimePartition(t, t, endInstant + 1, allTimeEndInstant);
            } else {
                partAfter.removeInstant(t, delta);
            }
            this.setDepth(Math.max(this.getDepth(), this.partAfter.getDepth() + 1));
        }

        if (startInstant - allTimeStartInstant < delta) {
            this.startInstant = this.allTimeStartInstant;
            this.partBefore = null;
            if (this.partAfter != null) {
                this.setDepth(this.partAfter.getDepth() + 1);
            } else {
                this.setDepth(1);
            }
        }

        if (allTimeEndInstant - endInstant < delta) {
            this.endInstant = this.allTimeEndInstant;
            this.partAfter = null;
            if (this.partBefore != null) {
                this.setDepth(this.partBefore.getDepth() + 1);
            } else {
                this.setDepth(1);
            }
        }
        if ((this.partBefore != null) && (this.partBefore.getEndInstant() + 1 == this.startInstant)) {
            this.mergeWithSonLeft();
        }
        if ((this.partAfter != null) && (this.partAfter.getStartInstant() - 1 == this.endInstant)) {
            this.mergeWithSonRight();
        }

        this.balanceThatTree();

    }

    public void balanceThatTree() {
        int depthLeft = 0;
        int depthRight = 0;

        if (this.partBefore != null) {
            depthLeft = this.partBefore.getDepth();
        }

        if (this.partAfter != null) {
            depthRight = this.partAfter.getDepth();
        }

        if (depthLeft > depthRight + 1) {
            rotateRight();
        } else if (depthLeft + 1 < depthRight) {
            rotateLeft();
        }
    }

    public void rotateRight() {
        TimePartition oldRoot = new TimePartition(this.startInstant, this.endInstant, this.partBefore.getEndInstant()
                + 1, this.allTimeEndInstant);
        oldRoot.setPartAfter(this.partAfter);
        for (Vertex u: this.getSplitters().getVertices()) {
            oldRoot.addSplitter(u);
        }


        TimePartition newRoot = new TimePartition(this.partBefore.getStartInstant(), this.partBefore.getEndInstant(),
                this.partBefore.getAllTimeStartInstant(), this.getAllTimeEndInstant());
        newRoot.setPartBefore(this.partBefore.getPartBefore());
        for (Vertex u: this.partBefore.getSplitters().getVertices()) {
            newRoot.addSplitter(u);
        }

        if (this.partBefore.getPartAfter() != null) {
            oldRoot.setPartBefore(this.partBefore.getPartAfter());
        }
        newRoot.setPartAfter(oldRoot);

        oldRoot.setDepth(1);
        if (oldRoot.getPartBefore() != null) {
            oldRoot.setDepth(Math.max(oldRoot.getDepth(), oldRoot.getPartBefore().getDepth() + 1));
        }

        if (oldRoot.getPartAfter() != null) {
            oldRoot.setDepth(Math.max(oldRoot.getDepth(), oldRoot.getPartAfter().getDepth() + 1));
        }

        newRoot.setDepth(Math.max(1, oldRoot.getDepth() + 1));

        if (newRoot.getPartBefore() != null) {
            newRoot.setDepth(Math.max(newRoot.getDepth(), newRoot.getPartBefore().getDepth() + 1));
        }

        this.setPartAfter(newRoot.getPartAfter());
        this.setPartBefore(newRoot.getPartBefore());
        this.setAllTimeEndInstant(newRoot.getAllTimeEndInstant());
        this.setAllTimeStartInstant(newRoot.getAllTimeStartInstant());
        this.setEndInstant(newRoot.getEndInstant());
        this.setStartInstant(newRoot.getStartInstant());
        this.setDepth(newRoot.getDepth());
        this.splitters = newRoot.getSplitters();
    }

    public void rotateLeft() {
        TimePartition oldRoot = new TimePartition(this.startInstant, this.endInstant, this.allTimeStartInstant, this.partAfter.getStartInstant() - 1);
        oldRoot.setPartAfter(this.partAfter);
        for (Vertex u: this.getSplitters().getVertices()) {
            oldRoot.addSplitter(u);
        }


        TimePartition newRoot = new TimePartition(this.partAfter.getStartInstant(), this.partAfter.getEndInstant(),
                this.getAllTimeStartInstant(), this.partAfter.getAllTimeEndInstant());
        newRoot.setPartAfter(this.partAfter.getPartAfter());
        for (Vertex u: this.partAfter.getSplitters().getVertices()) {
            newRoot.addSplitter(u);
        }

        if (this.partAfter.getPartBefore() != null) {
            oldRoot.setPartAfter(this.partAfter.getPartBefore());
        }
        newRoot.setPartBefore(oldRoot);

        oldRoot.setDepth(1);
        if (oldRoot.getPartAfter() != null) {
            oldRoot.setDepth(Math.max(oldRoot.getDepth(), oldRoot.getPartAfter().getDepth() + 1));
        }

        if (oldRoot.getPartBefore() != null) {
            oldRoot.setDepth(Math.max(oldRoot.getDepth(), oldRoot.getPartBefore().getDepth() + 1));
        }

        newRoot.setDepth(Math.max(1, oldRoot.getDepth() + 1));

        if (newRoot.getPartAfter() != null) {
            newRoot.setDepth(Math.max(newRoot.getDepth(), newRoot.getPartAfter().getDepth() + 1));
        }

        this.setPartAfter(newRoot.getPartAfter());
        this.setPartBefore(newRoot.getPartBefore());
        this.setAllTimeEndInstant(newRoot.getAllTimeEndInstant());
        this.setAllTimeStartInstant(newRoot.getAllTimeStartInstant());
        this.setEndInstant(newRoot.getEndInstant());
        this.setStartInstant(newRoot.getStartInstant());
        this.setDepth(newRoot.getDepth());
        this.splitters = newRoot.getSplitters();
    }

    public void mergeWithSonLeft() {
        this.startInstant = partBefore.getStartInstant();
        TimePartition tmp;
        if (this.partBefore.getPartBefore() == null) {
            tmp = null;
        } else {
            tmp = this.partBefore.getPartBefore();
        }
        this.partBefore = tmp;
        this.setDepth(this.depth - 1);
    }

    public void mergeWithSonRight() {
        this.endInstant = partAfter.getEndInstant();
        TimePartition tmp;
        if (this.partAfter.getPartAfter() == null) {
            tmp = null;
        } else {
            tmp = this.partAfter.getPartAfter();
        }
        this.partAfter = tmp;
        this.setDepth(this.depth - 1);

    }

    public ArrayList<DeltaEdge> getAllDeltaIntervals(int delta) {
        ArrayList<DeltaEdge> response = new ArrayList<>();
        if (partBefore == null) {
            if (this.startInstant - this.allTimeStartInstant >= delta) {
                DeltaEdge e = new DeltaEdge(this.allTimeStartInstant, this.startInstant - 1, null, null);
                response.add(e);
            }
        } else {
            response.addAll(this.partBefore.getAllDeltaIntervals(delta));
        }

        if (partAfter == null) {
            if (this.allTimeEndInstant - this.endInstant >= delta) {
                DeltaEdge e = new DeltaEdge(this.endInstant + 1, this.allTimeEndInstant, null, null);
                response.add(e);
            }
        } else {
            response.addAll(this.partAfter.getAllDeltaIntervals(delta));
        }

        return response;
    }

    public void recursivelyChangeStartInstant(int t) {
        if (this.partBefore != null) {
            this.partBefore.recursivelyChangeStartInstant(t);
        }
        this.allTimeStartInstant = t;
    }

    public void recursivelyChangeEndInstant(int t) {
        if (this.partBefore != null) {
            this.partBefore.recursivelyChangeEndInstant(t);
        }
        this.allTimeEndInstant = t;
    }
}
