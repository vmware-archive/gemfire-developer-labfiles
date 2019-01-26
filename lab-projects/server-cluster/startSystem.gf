
start locator --name=locator 

configure pdx --read-serialized=true

start server --server-port=0  --locators=localhost[10334]  --name=server1
start server --server-port=0  --locators=localhost[10334]  --name=server2
start server --server-port=0  --locators=localhost[10334]  --name=server3
