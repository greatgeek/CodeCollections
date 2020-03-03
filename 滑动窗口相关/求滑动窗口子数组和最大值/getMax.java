    public int getMax(int[] arr, int X){ // X is window size
        int max = 0;
        int sum = 0;
        for(int i=0;i<arr.length;i++){
            sum += arr[i];
            if(i>=X){
                sum -= arr[i-X]; 
            }
            max = Math.max(max,sum);
        }
        return max;
    }