import java.io.*;
//import java.io.FileNotFoundException;
import java.util.Scanner;
class Node{
    Node next;
    int start;
    int end;
    Node previous;
        Node(int s,int e) {
    	this.start=s;
    	this.end=e;
    	this.next=null;
    	this.previous=null;
    }
}
class linkedlist{   //creating a singly linked list

    int n;
    Node head; // creating two end point references
    Node tail;
    linkedlist(){
        this.head=null;
        this.tail=null;
        n=0;     
    }
    void addpixel(int start,int end){  //adding pixel at the end of linked list
        if(head==null){
            Node t=new Node(start,end);
            head=t;
            tail=head;
            n++;
            return;
        } 
        Node t=new Node(start,end);
        t.previous=tail;
        tail.next=t;
        tail=t;
        n++;
    } 
    void insertafter(Node node,int start,int end){
        Node n=new Node(start,end);
        Node nex=node.next;
        n.next=nex;
        nex.previous=n;
        n.previous=node;
        node.next=n;
        this.n++;
        
    }
    void insertfirst(int start,int end){
        if(n==0){
            Node t=new Node(start,end);
            head=t;
            this.tail=head;
            n++;
            return;
        }
        Node t=new Node(start,end);
        t.next=head;
        head.previous=t;
        head=t;
        this.n++;
    }
    void deletelast(){
        if(head.next == null){
           head = null;
           n--;
        }
        else{
            tail.previous.next = null; 
            tail = tail.previous;
            n--; 
        }
    }
    void delete(Node node){
        if(node==null||this.head==null){
            return;
        }
        if(this.head==node){
            head=node.next;
            n--;
        }
        if(node.next!=null){
            node.next.previous=node.previous;
        }
        if(node.previous!=null){
            node.previous.next=node.next;
        }
        return;
    }
    int size(){                     //return the size of the linked list
        return n;
    	}    
} 
   
public class LinkedListImage implements CompressedImageInterface {
        linkedlist[] list;
        private int h;
        private int w;
        public LinkedListImage(int hi){
            list=new linkedlist[hi];
            h=w=hi;
            for(int i=0;i<hi;i++){
                list[i]=new linkedlist();
            }
        }
        public LinkedListImage(String filename) throws FileNotFoundException{ 
      //  File file = new File(filename);
        FileReader f=new FileReader(filename);            
        Scanner s = new Scanner(f);
        w=s.nextInt();
        h=s.nextInt();
        list=new linkedlist[h];
        for(int i=0;i<h;i++){
            list[i]=new linkedlist();
        }
        for(int i=0;i<h;i++){
            int start=0;
            int end=0;
            int counter=0;
            for(int k=0;k<w;k++){
            int val=s.nextInt();
                if(val==0){
                    end=k;
                    counter=counter+1;
                    if(end==w-1){
                        start=end-counter+1;
                        counter=0;
                        list[i].addpixel(start,end);
                    }
                }
                else{
                	if(counter!=0) {
                    start=end-counter+1;
                    list[i].addpixel(start,end);
                    counter=0;
                    }
                }
            }
        }
        s.close();       
    }
    public LinkedListImage(boolean[][] grid, int width, int height)
    {	
        h=grid.length;
        w=grid[0].length;
        list=new linkedlist[h];
		for(int i=0;i<h;i++){
            list[i]= new linkedlist();
            int start=0;
            int end=0;
            int counter=0;
            for(int k=0;k<w;k++){
            boolean val=grid[i][k];
                if(val==false){
                    end=k;
                    counter=counter+1;
                    if(end==w-1){
                        start=end-counter+1;
                        counter=0;
                        list[i].addpixel(start,end);
                    }
                }
                else{
                	if(counter!=0) {
                    start=end-counter+1;
                    list[i].addpixel(start,end);
                    counter=0;
                    }
                }
            }
        }
    }

     public boolean getPixelValue(int x, int y) throws PixelOutOfBoundException{
        if(x>list.length|| x<0|| y<0 || y>list.length){
            throw new PixelOutOfBoundException("Error");
        }
        Node current=list[x].head;
        if(current==null) return true;
        else{
        while(current!=null){
            if(y>=current.start && y<=current.end){
                return false;
            }
            current=current.next;
        }
		return true;
    }
    }

    public void setPixelValue(int x, int y, boolean val) throws PixelOutOfBoundException
    {
        if(x>list.length|| x<0|| y<0 || y>list.length){
            throw new PixelOutOfBoundException("Error");
        }
        if(this.getPixelValue(x, y)!=val){
          if(val==true){
              int st,en;
              Node current=list[x].head;
              while(current!=null){
                    st=current.start;
                    en=current.end;
                    if(st==en&&y==st){
                        list[x].delete(current);
                        break;
                    }
                    if(y>=st&&y<=en){
                        if(y==st){
                            current.start+=1;
                            break;
                      }
                        if(y==en){
                            current.end-=1;
                            break;
                      }
                        if(y>st&&y<en){
                            current.end=y-1;
                            if(current.next==null){
                            list[x].addpixel(y+1,en);
                            }
                            else{
                                list[x].insertafter(current,y+1,en);
                            }
                            break;
                      }
                    }
                current=current.next;
              }
          }
          else{
              Node current=list[x].head;
              if(current==null){
                  list[x].addpixel(y,y);
                  return;
              }
             else if(y<current.start){
                if(y==current.start-1){
                    current.start-=1;
                }
                else if(y<current.start-1){
                    list[x].insertfirst(y, y);
                }
              }
              else if(y>list[x].tail.end){
                  if(y==list[x].tail.end+1){
                      list[x].tail.end+=1;
                  }
                  else if(y>list[x].tail.end+1){
                      list[x].addpixel(y, y);
                  }
              }
              else{

                  while(current.next!=null){
                      if(y>current.end+1 && y<current.next.start-1){
                          list[x].insertafter(current,y,y);
                          break;
                      }
                      if(y==current.end+1&&y<current.next.start-1){
                          current.end+=1;
                          break;
                      }
                      if(y>current.end+1&&y==current.next.start-1){
                          current.next.start-=1;
                          break;
                      }
                      if(y==current.next.start-1 && y==current.end+1){
                          current.next.start=current.start;
                          list[x].delete(current);
                          break;
                      }
                      current=current.next;
                  }
              }

            /*else{
                while(current!=null){
                //  if(y==en+1){
                //      current.end+=1;
                //      break;
                    if(current.next!=null){
                        if(y<current.next.start){
                            if(current.next.start-current.end==2){
                                current.end=current.next.end;
                                current.next.next.previous=current;
                                current.next=current.next.next;
                                break;
                            }
                            else if(y==current.end+1){
                                current.end+=1;
                                break;
                            }
                            else{
                                list[x].insertafter(current, y, y);  
                                break;                              
                            }
                        }
                        else{
                            current=current.next;
                        }
                    }
                    else{
                        if(y==current.end+1){
                            current.end+=1;
                            break;
                        }
                        else{
                         list[x].addpixel(y, y);
                         break;
                        }
                    }  

                } 
            } 
        */}
    }
}


    public int[] numberOfBlackPixels()
    {
		int[] bp=new int[h];
        for(int i=0;i<h;i++){
            int sum=0;
            Node current=list[i].head;
            if(current==null) sum=0;
            else{
            while(current!=null){
                sum+=current.end-current.start+1;
                current=current.next;
            }
        }
        bp[i]=sum;
        }
        return bp;
    }
    
    public void invert()
    {
        //System.out.println(this.toStringCompressed());
    	for(int j=0;j<h;j++){
            
            Node current= list[j].head;
            
            //int len=list[j].size();
            //linkedlist temp=new linkedlist();
            /*if(current==null){
                temp.addpixel(0,h-1);
                this.list[j]=temp;
                continue;
            }
            if(current.start==0&&current.end==h-1){
                this.list[j]=temp;
                continue;
            }
            if(current.head==0)
            */
            	if(current==null) {
                    list[j].addpixel(0,h-1);
                    continue;
                }
                //if(j==9)System.out.println("hi");
                if(list[j].head.start==0 &&list[j].head.end==h-1){
                    list[j].head=null;
                    list[j].tail=null;
                    continue;
                }
                if(list[j].head.start!=0&&list[j].head.end==h-1){
                    current.end=current.start-1;
                    current.start=0;
                    continue;
                }
                //if(j==9)System.out.println("hi");
                if(list[j].head.start==0 && list[j].tail.end!=h-1){
                    while(current!=null){
                        if(current.next==null){
                            current.start=current.end+1;
                            current.end=h-1;
                            current=current.next;
                            continue;
                        }
                        else{
                            current.start=current.end+1;
                            current.end=current.next.start-1;
                            current=current.next;   
                        }
                    }
                    continue;
                }
                //if(j==9)System.out.println("hi");
                if(list[j].head.start!=0 && list[j].tail.end==h-1){
                    int en=current.end;
                    int st=current.start;
                    current.end=current.start-1;
                    current.start=0;
                    current=current.next;
                    while(current!=null) {                    	
                    	st=current.start;
                    	current.start=en+1;
                    	en=current.end;
                    	current.end=st-1;
                    	current=current.next;
                    }
                    continue;
                }
                //if(j==9)System.out.println("hi");
                if(list[j].head.start==0&&list[j].tail.end==h-1) {
                	while(current.next!=null) {
                		current.start=current.end+1;
                		current.end=current.next.start-1;
                		current=current.next;
                    }
                    list[j].deletelast();
                    continue;
                }
                //if(j==9)System.out.println("hi");
                if(list[j].head.start!=0&&list[j].tail.end!=h-1) {
                	int st=current.start;
                    int en=current.end;
                    current.end=current.start-1;
                    current.start=0;
                	current=current.next;
                	while(current!=null){
                		st=current.start;
                		current.start=en+1;
                		en=current.end;
                		current.end=st-1;
                		current=current.next;
                    }

                	//list[j].head.end=list[j].head.start-1;
                    //list[j].head.start=0;
                    list[j].addpixel(en+1,h-1);
                    continue;
                
                }
            
        }
		
    }
    public void performAnd(CompressedImageInterface img) throws BoundsMismatchException 
    {
        LinkedListImage im=(LinkedListImage) img;
        int heightimg=im.list.length;
        int height=this.list.length;
        if(height!=heightimg){
            throw new BoundsMismatchException("Error");
        }   
        /*LinkedListImage img1=new LinkedListImage(height);
        for(int j=0;j<height;j++){
            Node node=im.list[j].head;
            while(node!=null){
                img1.list[j].addpixel(node.start,node.end);
                node=node.next;
            }
        }
        //System.out.println("A  before invert: "+this.toStringCompressed());
        this.invert(); //a'
        //System.out.println("A  after invert:  "+this.toStringCompressed());
        //System.out.println("hello");
        //System.out.println("B  before invert: "+img1.toStringCompressed());
        img1.invert();// b'
        //System.out.println("B after invert:   "+img1.toStringCompressed());
        //System.out.println("Ainv before OR:   "+this.toStringCompressed());
        //System.out.println("hi");
        this.performOr(img1); //a'+b'
        //System.out.println("Ainv OR Binv:      "+this.toStringCompressed());
        //System.out.println("hello");
        //System.out.println("hello again");
        //System.out.println("1");
        //System.out.println("this:before invert "+this.toStringCompressed());
        this.invert();
        //System.out.println("(AinvORBinv)inv is:"+img1.toStringCompressed());
        //System.out.println("hi");
        */
        for(int i=0;i<height;i++){
            for(int j=0;j<height;j++){
                try{
                if(this.getPixelValue(i, j)==true&&im.getPixelValue(i, j)==true){
                    this.setPixelValue(i, j, true);
                }
                else{
                    this.setPixelValue(i, j, false);
                }
            }
            catch(PixelOutOfBoundException e){
                    System.out.println(" ");
            }
        }
    }
    }
    
    public void performOr(CompressedImageInterface img) throws BoundsMismatchException
    {
        LinkedListImage im=(LinkedListImage)img;
        int heightimg=im.list.length;
        int height=this.list.length;
        if(heightimg!=height){
            throw new BoundsMismatchException("Error");
        }
        /*int s1;
        int s2;
        int e1;
        int e2;
        linkedlist temp;
        //linkedlist temp=new linkedlist();
        for(int i=0;i<height;i++){
            temp=new linkedlist();
            Node node1=this.list[i].head;
            Node node2=im.list[i].head;
            if(node1==null||node2==null){
                temp=null;
            }
            else {
            while(node1!=null&&node2!=null){
                //System.out.println("Inside while");
                s1=node1.start;
                e1=node1.end;
                s2=node2.start;
                e2=node2.end;
                if(s2>=s1&&s2<=e1&&e2>=e1){
                    temp.addpixel(s2,e1);
                    node1=node1.next;
                    //print(temp);
                    continue;
                }
                if(s1>=s2&&s1<=e2&&e1>=e2){
                    temp.addpixel(s1, e2);
                    node2=node2.next;
                    //print(temp);
                    //System.out.println(temp.start+" "+temp.end);
                    continue;                 
                }
                if(s1>=s2&&e1<=e2){
                    temp.addpixel(s1, e1);
                    node1=node1.next;
                    //print(temp);
                    //System.out.println(temp.start+" "+temp.end);
                    continue;
                }
                if(s2>=s1&&e2>=e1){
                    temp.addpixel(s2, e2);
                    node2=node2.next;
                    //print(temp);
                    //System.out.println(temp.start+" "+temp.end);
                    continue;
                }
                if(e1<s2){
                    node1=node1.next;
                    continue;

                }
                if(e2<s1){
                    node2=node2.next;
                    continue;
                }
            }
            list[i]=temp;
        }
        }
        */
        for(int i=0;i<height;i++){
            for(int j=0;j<height;j++){
                try{
                if(this.getPixelValue(i, j)==false&&im.getPixelValue(i, j)==false){
                    this.setPixelValue(i, j, false);
                }
                else{
                    this.setPixelValue(i, j, true);
                }
            }
            catch(PixelOutOfBoundException e){
                    System.out.println(" ");
            }
        }
    }

    }
    
    public void performXor(CompressedImageInterface img) throws BoundsMismatchException
    {	
        LinkedListImage im=(LinkedListImage) img;
        int heightimg=im.list.length;
        int height=this.list.length;
        //System.out.println(heightimg+" "+height);
        if(height!=heightimg){
            throw new BoundsMismatchException("");
        }
        /*LinkedListImage img1=new LinkedListImage(height);
        for(int j=0;j<height;j++){
            Node current=im.list[j].head;
            while(current!=null){
                img1.list[j].addpixel(current.start,current.end);
                current=current.next;
            }
            //System.out.println(img1.list[j].head);
        }
        //System.out.println(img1.list[1].head.start);
        LinkedListImage img2=new LinkedListImage(height);
        for(int j=0;j<height;j++){
            Node node=this.list[j].head;
            while(node!=null){
                img2.list[j].addpixel(node.start,node.end);
                node=node.next;
            }
        }
       //System.out.println("inv start ");
        //System.out.println("b   : "+im.toStringCompressed());
        //System.out.println("b   : "+img1.toStringCompressed());
        img1.invert();
        //System.out.println("b_  : "+img1.toStringCompressed());
        //System.out.println("a   : "+this.toStringCompressed());
        //System.out.println(img1.list.length);
        //System.out.println("hi first");
        this.performAnd(img1);//System.out.println("and e");
        //System.out.println("ab_ : "+this.toStringCompressed());
        img2.invert();
        //System.out.println("a_  : "+img2.toStringCompressed());
        //System.out.println("b   : "+im.toStringCompressed());
        img2.performAnd(im);
        //System.out.println("a_b : "+ img2.toStringCompressed());
        //System.out.println("this before or: "+ this.toStringCompressed());
        this.performOr(img2);
        //System.out.println("ab_+a_b : "+ this.toStringCompressed());
        */
        for(int i=0;i<height;i++){
            for(int j=0;j<height;j++){
                try{
                if(this.getPixelValue(i, j)==im.getPixelValue(i, j)){
                    this.setPixelValue(i, j, false);
                }
                else{
                    this.setPixelValue(i, j, true);
                }
            }
            catch(PixelOutOfBoundException e){
                    System.out.println(" ");
            }
        }
    }
}
    
    public String toStringUnCompressed()
    {
		String str="";
        str=str+w+" "+h+",";
        for(int i=0;i<h;i++){
            Node current=list[i].head;
            /*for(int j=0;j<list[i].size();j++){
                if(current==null){
                    str=str+" 1";
                }
                else{
                if(j<current.start){
                    str=str+w;
                }
                else{
                    if(j>current.start && j<current.end){
                        str=str+b;
                    }
                    else{
                        current=current.next;
                        str=str+w;
                    }
                }
                }
            }
                str+=",";
            */
            for(int j=0;j<w;j++){
                if(current==null){
                    str+=" 1";
                    continue;
                }
                else{
                    int st=current.start;
                    int en=current.end;
                    if(j>=st&&j<=en){
                        str+=" 0";
                        continue;
                    }
                    if(j>en||j<st){
                        str+=" 1";
                        if(j>en){
                        current=current.next;
                        }
                        continue;
                    }
                }
            }
            str+=",";
            }
            str=str.substring(0,str.length()-1);
            return str;
        }

    
    
    public String toStringCompressed()
    {
        String str="";
        str=str+w+" "+h+", ";
        //System.out.println(h);
        for(int i=0;i<h;i++){
            Node current=list[i].head;
            if(current==null){
                str+="-1, ";
            }
            else{
            while(current!=null){
                str=str+current.start+" "+current.end+" ";
                current=current.next;
            }
            
            str=str+"-1, ";
        }
        }
        str=str.substring(0,str.length()-2);
        return str;   		
    }

    public static void main(String[] args) throws FileNotFoundException {
    	// testing all methods here :
        boolean success = true;
        
    	// check constructor from file
    	CompressedImageInterface img1 = new LinkedListImage("sampleInputFile.txt");
    	// check toStringCompressed
        String img1_compressed = img1.toStringCompressed();
    	String img_ans = "16 16, -1, 5 7 -1, 3 7 -1, 2 7 -1, 2 2 6 7 -1, 6 7 -1, 6 7 -1, 4 6 -1, 2 4 -1, 2 3 14 15 -1, 2 2 13 15 -1, 11 13 -1, 11 12 -1, 10 11 -1, 9 10 -1, 7 9 -1";
    	success = success && (img_ans.equals(img1_compressed));

    	if (!success)
    	{
    		System.out.println("Constructor (file) or toStringCompressed ERROR");
    		return;
    	}

    	// check getPixelValue
    	boolean[][] grid = new boolean[16][16];
    	for (int i = 0; i < 16; i++)
    		for (int j = 0; j < 16; j++)
    		{
                try
                {
        			grid[i][j] = img1.getPixelValue(i, j);                
                }
                catch (PixelOutOfBoundException e)
                {
                    System.out.println("Errorrrrrrrr");
                }
    		}

    	// check constructor from grid
    	CompressedImageInterface img2 = new LinkedListImage(grid, 16, 16);
        String img2_compressed = img2.toStringCompressed();
    	success = success && (img2_compressed.equals(img_ans));

    	if (!success)
    	{
    		System.out.println("Constructor (array) or toStringCompressed ERROR");
    		return;
        }
        
    	// check Xor
        try
        {
            img1.performXor(img2);
            //System.out.println("XORED: " +image.toStringCompressed());
            //System.out.println(" lol1");
        }
        catch (BoundsMismatchException e)
        {
            System.out.println("Errorrrrrrrr");
        }
        
    	for (int i = 0; i < 16; i++)
    		for (int j = 0; j < 16; j++)
    		{
                try
                {
        			success = success && (!img1.getPixelValue(i,j));                
                }
                catch (PixelOutOfBoundException e)
                {
                    System.out.println("Errorrrrrrrr");
                }
    		}

    	if (!success)
    	{
    		System.out.println("performXor or getPixelValue ERROR");
    		return;
        }
        
    	// check setPixelValue
    	for (int i = 0; i < 16; i++)
        {
            try
            {
    	    	img1.setPixelValue(i, 0, true);            
            }
            catch (PixelOutOfBoundException e)
            {
                System.out.println("Errorrrrrrrr");
            }
        }

        
    	// check numberOfBlackPixels
    	int[] img1_black = img1.numberOfBlackPixels();
    	success = success && (img1_black.length == 16);
    	for (int i = 0; i < 16 && success; i++)
    		success = success && (img1_black[i] == 15);
    	if (!success)
    	{
    		System.out.println("setPixelValue or numberOfBlackPixels ERROR");
    		return;
        }

    	// check invert
        img1.invert();
        for (int i = 0; i < 16; i++)
        {
            try
            {
                success = success && !(img1.getPixelValue(i, 0));            
            }
            catch (PixelOutOfBoundException e)
            {
                System.out.println("Errorrrrrrrr");
            }
        }
        if (!success)
        {
            System.out.println("invert or getPixelValue ERROR");
            return;
        }

    	// check Or
        try
        {
            img1.performOr(img2);        
        }
        catch (BoundsMismatchException e)
        {
            System.out.println("Errorrrrrrrr");
        }
        for (int i = 0; i < 16; i++)
            for (int j = 0; j < 16; j++)
            {
                try
                {
                    success = success && img1.getPixelValue(i,j);
                }
                catch (PixelOutOfBoundException e)
                {
                    System.out.println("Errorrrrrrrr");
                }
            }
        if (!success)
        {
            System.out.println("performOr or getPixelValue ERROR");
            return;
        }
    
        // check And
        try
        {
            img1.performAnd(img2);    
        }
        catch (BoundsMismatchException e)
        {
            System.out.println("Errorrrrrrrr");
        }
        for (int i = 0; i < 16; i++)
            for (int j = 0; j < 16; j++)
            {
                try
                {
                    success = success && (img1.getPixelValue(i,j) == img2.getPixelValue(i,j));             
                }
                catch (PixelOutOfBoundException e)
                {
                    System.out.println("Errorrrrrrrr");
                }
            }
        if (!success)
        {
            System.out.println("performAnd or getPixelValue ERROR");
            return;
        }
    	// check toStringUnCompressed
        String img_ans_uncomp = "16 16, 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1, 1 1 1 1 1 0 0 0 1 1 1 1 1 1 1 1, 1 1 1 0 0 0 0 0 1 1 1 1 1 1 1 1, 1 1 0 0 0 0 0 0 1 1 1 1 1 1 1 1, 1 1 0 1 1 1 0 0 1 1 1 1 1 1 1 1, 1 1 1 1 1 1 0 0 1 1 1 1 1 1 1 1, 1 1 1 1 1 1 0 0 1 1 1 1 1 1 1 1, 1 1 1 1 0 0 0 1 1 1 1 1 1 1 1 1, 1 1 0 0 0 1 1 1 1 1 1 1 1 1 1 1, 1 1 0 0 1 1 1 1 1 1 1 1 1 1 0 0, 1 1 0 1 1 1 1 1 1 1 1 1 1 0 0 0, 1 1 1 1 1 1 1 1 1 1 1 0 0 0 1 1, 1 1 1 1 1 1 1 1 1 1 1 0 0 1 1 1, 1 1 1 1 1 1 1 1 1 1 0 0 1 1 1 1, 1 1 1 1 1 1 1 1 1 0 0 1 1 1 1 1, 1 1 1 1 1 1 1 0 0 0 1 1 1 1 1 1";
        success = success &&(img1.toStringUnCompressed().equals(img_ans_uncomp))&&(img2.toStringUnCompressed().equals(img_ans_uncomp));
        if (!success)
        {
            System.out.println("toStringUnCompressed ERROR");
            return;
        }
        else
            System.out.println("ALL TESTS SUCCESSFUL! YAYY!");
            
    }
}
//&& (img1.toStringUnCompressed().equals(img_ans_uncomp)) &&
//(img2.toStringUnCompressed().equals(img_ans_uncomp))
	