一、
```java
Map<String,Integer> map = new HashMap<>();

for(Map.Entry<String,Integer> entry : map.entrySet()){
    map.put(entry.getKey(),map.getOrDefault(entry.getKey(),0)+1);
}
```

二、
```java
Map<String,Integer> map = new HashMap<>();
for(String str : map.keySet()){
    int value = map.get(str);
    map.put(str,map.getOrDefault(str,0)+1);
}
```