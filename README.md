Bencode
========

This is an implementation of Bencode for Java. Bencode is used for DHTs, Torrents, and Google DataServers. Its a lightweight fast data serialization.
[Wikipedia](https://en.wikipedia.org/wiki/Bencode)

I have also made an implementation of Bencode with [Rust](https://github.com/DrBrad/BencodeRust) [JavaScript](https://github.com/DrBrad/BencodeJS) and [PHP](https://github.com/DrBrad/BencodePHP).

Benchmarks
-----
Here are some examples of this library compared to other major data serialization methods.

Serialization / Encoding
| Method  | Time in Mills |
| ------------- | ------------- |
| Bencode  | 57  |
| JSON  | 230  |

Parsing
| Method  | Time in Mills |
| ------------- | ------------- |
| Bencode  | 83  |
| JSON  | 281  |

Byte Size when encoded
| Method  | Bytes |
| ------------- | ------------- |
| Bencode  | 10134606  |
| JSON  | 51538417  |

Library
-----
The JAR for the library can be found here: [Bencode JAR](https://github.com/DrBrad/Bencode/blob/main/out/artifacts/Bencode_jar/Bencode.jar?raw=true)

Usage
-----
Here are some examples of how to use the Bencode library.

**Bencode Array**
```Java
//FROM LIST
ArrayList<String> l = new ArrayList<>();
BencodeArray bar = new BencodeArray(l);

//FROM BYTES
byte[] b; //ARRAY OF BYTES
BencodeArray bar = new BencodeArray(b);

//CREATE BENCODE
BencodeArray bar = new BencodeArray();
```

**Bencode Object | Map**
```Java
//FROM MAP
HashMap<String, String> l = new HashMap<>();
BencodeObject bob = new BencodeObject(l);

//FROM BYTES
byte[] b; //ARRAY OF BYTES
BencodeObject bob = new BencodeObject(b);

//CREATE BENCODE
BencodeObject bob = new BencodeObject();
```

**Put | Get data**
```Java
//ARRAY
bar.put(1000);
bar.get(0);

//MAP
bob.put("KEY", 100);
bob.get("KEY");
```

**Encoding to byte array**
```Java
bar.encode();
```

**Readable String**
```Java
System.out.println(bar.toString());
```
