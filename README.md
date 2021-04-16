Bencode
========

This is an implementation of Bencode for Java. Bencode is used for DHTs, Torrents, and Google DataServers. Its a lightweight fast data serialization.
[Wikipedia](https://en.wikipedia.org/wiki/Bencode)

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
//FROM Map
HashMap<String, String> l = new HashMap<>();
BencodeObject bob = new BencodeObject(l);

//FROM BYTES
byte[] b; //LIST OF BYTES
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

License
-----------
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, TITLE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR ANYONE DISTRIBUTING THE SOFTWARE BE LIABLE FOR ANY DAMAGES OR OTHER LIABILITY, WHETHER IN CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE
