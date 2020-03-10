// 版本一

/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
public class Codec {

    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
        if(root==null){
            return "#_";
        }
        String res = root.val+"_"; // 仅加后缀
        res += serialize(root.left);
        res += serialize(root.right);
        return res;
    }

    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        String[] values = data.split("_"); // 分割时不会出现空字符
        Queue<String> queue = new LinkedList<String>();
        for(int i=0;i!=values.length;i++){
            queue.offer(values[i]);
        }
        return reconPreOrder(queue);
    }
    
    public TreeNode reconPreOrder(Queue<String> queue){
        String value = queue.poll();
        if(value.equals("#")){
            return null;
        }
        
        TreeNode root = new TreeNode(Integer.valueOf(value));
        root.left = reconPreOrder(queue);
        root.right = reconPreOrder(queue);
        return root;
    }
}

// Your Codec object will be instantiated and called as such:
// Codec codec = new Codec();
// codec.deserialize(codec.serialize(root));

/***************************************************************************************/
// 版本二

/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
public class Codec {

    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
        if(root==null){
            return "_#_";
        }
        String res = "_"+root.val+"_"; // 加了前缀和后缀
        res += serialize(root.left);
        res += serialize(root.right);
        return res;
    }

    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        String[] values = data.split("_+"); // 利用正则表达式分割字符，第一个字符为空字符
        Queue<String> queue = new LinkedList<String>();
        for(int i=1;i!=values.length;i++){
            queue.offer(values[i]);
        }
        return reconPreOrder(queue);
    }
    
    public TreeNode reconPreOrder(Queue<String> queue){
        String value = queue.poll();
        if(value.equals("#")){
            return null;
        }
        
        TreeNode root = new TreeNode(Integer.valueOf(value));
        root.left = reconPreOrder(queue);
        root.right = reconPreOrder(queue);
        return root;
    }
}

// Your Codec object will be instantiated and called as such:
// Codec codec = new Codec();
// codec.deserialize(codec.serialize(root));