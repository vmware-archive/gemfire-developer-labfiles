
start locator --name=locator 

start server --server-port=0  --locators=localhost[10334] --name=server1
start server --server-port=0  --locators=localhost[10334] --name=server2
start server --server-port=0  --locators=localhost[10334] --name=server3
