# Kruskal 算法

```java

public class Question1 {

    // 边
    private static class Edge{
        int start;
        int end;
        long weight;

        public Edge(int start,int end,long weight){
            this.start=start;
            this.end=end;
            this.weight=weight;
        }
    }


    // 计算排列组合数
    static long combination(int n,int k){
        int a=1,b=1;
        if(k>n/2){
            k=n-k;
        }
        for(int i=1;i<=k;++i){
            a *=(n+1-i);
            b*=i;
        }
        return a/b;
    }

    public static long func(int n,int m,Edge[] edges){
        // 按照最小权重作为代价进行排序
        Arrays.sort(edges,(a,b)-> {
            if(b.weight==a.weight) return 0;
            return a.weight<b.weight ? 1 : -1;
        });
        long res = createTree(n,m,edges);
        return res;
    }
	
    // 应用 Kruskal 算法来生成树
    private static long createTree(int n,int m,Edge[] edges){
        int[] parent = new int[n+1];

        long sum=0;
        int nodeCount=0;
        for(Edge edge : edges){
            // 判断加入该边是否会成环
            int start = find(parent,edge.start);
            int end = find(parent,edge.end);

            if(start !=end){
                nodeCount++;
                parent[start]=end;
                sum += edge.weight;
                sum %= 1000000007;
            }
        }

        if(nodeCount!=n-1) return -1;

        return sum;
    }

    // 获取集合的最后节点 (这是一个并查集)
    private static int find(int parent[] , int index){
        while (parent[index]>0){
            index = parent[index];
        }
        return index;
    }

    public static void main(String[] args) throws FileNotFoundException {
        FileInputStream fi = new FileInputStream("src\\neteasyEntertainment\\Question1.txt");
        Scanner sc = new Scanner(fi);

        int n=sc.nextInt();
        int m=sc.nextInt();
        Edge[] edges = new Edge[m];
        for(int i=0;i<m;++i){
            edges[i]=new Edge(sc.nextInt(),sc.nextInt(),combination(sc.nextInt(),sc.nextInt()));
        }
        System.out.println(func(n,m,edges));
    }
}

```

