import java.util.*;
import java.io.*;
import java.lang.*;

class TreeNode{
    public int xpoint;
    public int ypoint;
    public int restaurantInRange;
    public TreeNode left;
    public TreeNode right;
    public TreeNode parent;
    public int xmin;
    public int ymin;
    public int xmax;
    public int ymax;
    public boolean isLeaf;
}


class Pair<A, B>{
    public A First;
    public B Second;
    public Pair(){

    }
    public Pair(A _first, B _second) {
        this.First = _first;
        this.Second = _second;
    }
    public A get_first() {
        return First;
    }
    public B get_second() {
        return Second;
    }
}

class MerkleTree{
    public TreeNode rootnode;
    public int totalRestaurant;


    int min(int a,int b){
        if(a>=b)
            return b;
        else return a;
    }

    int max(int a,int b){
        if(a>=b)
            return a;
        else return b;
    }

    boolean Overlap(int l1x,int l1y, int r1x, int r1y, int l2x,int l2y, int r2x,int r2y)
    {
        if (l1x > r2x || l2x > r1x)
            return false;
        if (r1y > l2y || r2y > l1y)
            return false;

        return true;
    }

    public int Search(TreeNode node, int xminquery, int xmaxquery, int yminquery, int ymaxquery) {

        if(node==null)
            return 0;
        if (Overlap(xminquery,ymaxquery,xmaxquery,yminquery,node.xmin,node.ymax,node.xmax,node.ymin)==false)
            return 0;
        if (xminquery <= node.xmin && xmaxquery >= node.xmax && ymaxquery >= node.ymax && yminquery <= node.ymin)
            return node.restaurantInRange;
        else
        if (node.isLeaf == true) {
            if(xmaxquery >= node.xpoint && xminquery <= node.xpoint && ymaxquery >= node.ypoint && yminquery <= node.ypoint)
                return 1;
            else return 0;
        }
        else if(node.isLeaf==false) {
            int t=0,y=0;
            if(node.left!=null){
                int newxminquery,newxmaxquery,newyminquery,newymaxquery;
                newxminquery=max(xminquery,node.left.xmin);
                newxmaxquery=min(xmaxquery, node.left.xmax);
                newymaxquery=min(ymaxquery, node.left.ymax);
                newyminquery=max(yminquery, node.left.ymin);
                t=Search(node.left,newxminquery, newxmaxquery, newyminquery, newymaxquery);
            }
            if(node.right!=null){
                int newxminquery,newxmaxquery,newyminquery,newymaxquery;
                newxminquery=max(xminquery,node.right.xmin);
                newxmaxquery=min(xmaxquery, node.right.xmax);
                newymaxquery=min(ymaxquery, node.right.ymax);
                newyminquery=max(yminquery, node.right.ymin);
                y=Search(node.right,newxminquery, newxmaxquery, newyminquery, newymaxquery);
            }
            return t+y;
        }
        return 0;
    }

    public int RestaurantSearch(Pair<Integer, Integer> query){
        int xminquery=query.get_first()-100;
        int xmaxquery=query.get_first()+100;
        int yminquery=query.get_second()-100;
        int ymaxquery=query.get_second()+100;
        return Search(this.rootnode, xminquery, xmaxquery, yminquery, ymaxquery);
    }

    public void postOrder(TreeNode node) {
        if (node == null) {
            return;
        }
        postOrder(node.left);
        postOrder(node.right);
        if(node.left==null && node.right==null)
            node.isLeaf=true;
        else node.isLeaf=false;

        if(node.isLeaf==true)
            node.restaurantInRange=1;
        else {
            if(node.left==null)
                node.restaurantInRange=node.right.restaurantInRange;
            else if(node.right==null)
                node.restaurantInRange=node.left.restaurantInRange;
            else if(node.left!=null && node.left!=null)
                node.restaurantInRange=node.left.restaurantInRange+ node.right.restaurantInRange;}

    }

    public int findHeight(TreeNode node){
        int u=0;
        while(node!=null) {
            node = node.parent;
            u++;
        }
        return (u-1);
    }




    public TreeNode InsertDocument(Pair<Integer, Integer> point, int i, TreeNode inItPutNew, boolean leftOrRight) {
        TreeNode dummynode=new TreeNode();

        TreeNode node=new TreeNode();
        dummynode=inItPutNew;
        if(this.rootnode==null && i==0)
        {
            node.xpoint= point.get_first();
            node.ypoint= point.get_second();
            node.xmax=Integer.MAX_VALUE;
            node.ymax=Integer.MAX_VALUE;
            node.xmin=Integer.MIN_VALUE;
            node.ymin=Integer.MIN_VALUE;
            node.left=null;
            node.right=null;
            node.parent=null;
            rootnode=node;
        }
        else if(i%2!=0)
        {
            if(leftOrRight==true) {
                node.xpoint= point.get_first();
                node.ypoint= point.get_second();
                node.xmax=dummynode.xpoint;;
                node.ymax=dummynode.ymax;
                node.xmin=dummynode.xmin;
                node.ymin=dummynode.ymin;
                node.left=null;
                node.right=null;
                node.parent=dummynode;
                dummynode.left=node;
            }
            else if(leftOrRight==false) {
                node.xpoint= point.get_first();
                node.ypoint= point.get_second();
                node.xmax= dummynode.xmax;;
                node.ymax=dummynode.ymax;
                node.xmin=dummynode.xpoint+1;
                node.ymin=dummynode.ymin;
                node.left=null;
                node.right=null;
                node.parent=dummynode;
                dummynode.right=node;
            }
        }
        else if(i%2==0)
        {
            if(leftOrRight==true) {
                node.xpoint= point.get_first();
                node.ypoint= point.get_second();
                node.xmax= dummynode.xmax;;
                node.ymax=dummynode.ypoint;
                node.xmin=dummynode.xmin;
                node.ymin=dummynode.ymin;
                node.left=null;
                node.right=null;
                node.parent=dummynode;
                dummynode.left=node;
            }
            else if(leftOrRight==false) {
                node.xpoint= point.get_first();
                node.ypoint= point.get_second();
                node.xmax= dummynode.xmax;;
                node.ymax=dummynode.ymax;
                node.xmin=dummynode.xmin;
                node.ymin=dummynode.ypoint+1;
                node.left=null;
                node.right=null;
                node.parent=dummynode;
                dummynode.right=node;
            }
        }
        return node;
    }

    public List<Pair<Integer,Integer>> sortByX(List<Pair<Integer,Integer>> toBeSorted) {
if(toBeSorted==null)
return toBeSorted;
        int tempfirst = 0;
int tempsecond=0;  
         for(int i=0; i < toBeSorted.size(); i++){  
                 for(int j=1; j < (toBeSorted.size()-i); j++){  
                          if(toBeSorted.get(j-1).get_first() > toBeSorted.get(j).get_first()){   
                                 tempfirst = toBeSorted.get(j-1).First;  
                                 toBeSorted.get(j-1).First = toBeSorted.get(j).First;  
                                 toBeSorted.get(j).First = tempfirst;  
				tempsecond = toBeSorted.get(j-1).Second;  
                                 toBeSorted.get(j-1).Second = toBeSorted.get(j).Second;  
                                 toBeSorted.get(j).Second = tempsecond;
                         }  
                          
                 }  }
        return toBeSorted;
    }

    public List<Pair<Integer,Integer>> sortByY(List<Pair<Integer,Integer>> toBeSorted) {
if(toBeSorted==null)
return toBeSorted;
      int tempfirst = 0;
int tempsecond=0;  
         for(int i=0; i < toBeSorted.size(); i++){  
                 for(int j=1; j < (toBeSorted.size()-i); j++){  
                          if(toBeSorted.get(j-1).get_second() > toBeSorted.get(j).get_second()){   
                                 tempfirst = toBeSorted.get(j-1).First;  
                                 toBeSorted.get(j-1).First = toBeSorted.get(j).First;  
                                 toBeSorted.get(j).First = tempfirst;  
				tempsecond = toBeSorted.get(j-1).Second;  
                                 toBeSorted.get(j-1).Second = toBeSorted.get(j).Second;  
                                 toBeSorted.get(j).Second = tempsecond;
                         }  
                          
                 }  }
        return toBeSorted;
    }

    public void findInsertElement(List<Pair<Integer,Integer>> listOfPoint,int i, TreeNode inItGoAnother, boolean boolen) {
        if(listOfPoint.isEmpty()==true)
            return;
        else {
            int dummy1 = i, dummy2 = i;
            int median;
            MerkleTree tree = new MerkleTree();
            int size = listOfPoint.size();
            if (size % 2 == 0)
                median = (size / 2)-1;
            else median = (((size + 1) / 2))-1;
            if (i % 2 == 0)
                listOfPoint = sortByX(listOfPoint);
            else listOfPoint = sortByY(listOfPoint);
            TreeNode justmadenode=new TreeNode();
            if(i%2==0){
                while(median<listOfPoint.size()-1 && listOfPoint.get(median).get_first()==listOfPoint.get(median+1).get_first()){
                    median=median+1;
                }}
            else if(i%2!=0){
                while(median<listOfPoint.size()-1 && listOfPoint.get(median).get_second()==listOfPoint.get(median+1).get_second()){
                    median=median+1;
                }}
            justmadenode=this.InsertDocument(listOfPoint.get(median), i, inItGoAnother, boolen);
            List<Pair<Integer, Integer>> toleft = new ArrayList<Pair<Integer, Integer>>();
            List<Pair<Integer, Integer>> toright = new ArrayList<Pair<Integer, Integer>>();
            if(listOfPoint.size()!=1)
            {

                for (int h = 0; h <= median; h++)
                    toleft.add(listOfPoint.get(h));

                for (int h = median + 1; h < size; h++)
                    toright.add(listOfPoint.get(h));
            }

            findInsertElement(toleft, ++dummy1, justmadenode, true);
            findInsertElement(toright, ++dummy2, justmadenode, false);
        }
    }
}

public class kdtree{
    public static void main(String[] args) {

        int place=0,a=0,b=0,length=0;

//ADDING RESTAURANT IN A ARRAY LIST AS A PAIR OF THEIR COORDINATES
        List<Pair<Integer,Integer>> restaurantpoint = new ArrayList<Pair<Integer,Integer>>();
        File restaurant =new File("restaurants.txt");
        try
        {
            Scanner point = new Scanner(restaurant);
            point.nextLine();
            while(point.hasNextLine())
            {
                String points=point.nextLine();
                length = points.length();
                place=points.lastIndexOf(",");
                a=Integer.valueOf(points.substring(0,place));
                b=Integer.valueOf(points.substring(place+1,length));
                restaurantpoint.add(new Pair<Integer,Integer>(a,b));
            }
            point.close();
        }catch (FileNotFoundException e){
            System.out.println(e);
        }



//ADDING QUERIES IN A ARRAY LIST AS A PAIR OF THEIR COORDINATES
        List<Pair<Integer,Integer>> querypoint = new ArrayList<Pair<Integer,Integer>>();
        File query =new File("queries.txt");
        try
        {
            Scanner qpoint = new Scanner(query);
            qpoint.nextLine();
            while(qpoint.hasNextLine())
            {
                String points=qpoint.nextLine();
                length = points.length();
                place=points.lastIndexOf(",");
                a=Integer.valueOf(points.substring(0,place));
                b=Integer.valueOf(points.substring(place+1,length));
                querypoint.add(new Pair<Integer,Integer>(a,b));
            }
            qpoint.close();
        }catch (FileNotFoundException e){
            System.out.println(e);
        }


//MAKING THE TREE WITH RESTAURANT POINT
        MerkleTree tree = new MerkleTree();
        if (restaurantpoint == null) {
            tree.rootnode = null;
            tree.totalRestaurant = 0;
        }
        else {
            tree.findInsertElement(restaurantpoint, 0,tree.rootnode,true);
        }
        tree.postOrder(tree.rootnode);


        try{
            FileWriter outputs =new FileWriter("output.txt");
            for(int k=0;k<querypoint.size();k++){

                outputs.write(Integer.toString(tree.RestaurantSearch(querypoint.get(k))));
                outputs.write("\n");

            }
            outputs.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}

