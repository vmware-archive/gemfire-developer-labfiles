
start locator --name=locator 

configure pdx --read-serialized=true

start server --server-port=0  --locators=localhost[10334] --cache-xml-file=./xml/serverCache.xml --name=server1
start server --server-port=0  --locators=localhost[10334] --cache-xml-file=./xml/serverCache.xml --name=server2
start server --server-port=0  --locators=localhost[10334] --cache-xml-file=./xml/serverCache.xml --name=server3
