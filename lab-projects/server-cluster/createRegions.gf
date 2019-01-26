create region --name=BookMaster --type=REPLICATE --skip-if-exists
create region --name=Customer --type=PARTITION_REDUNDANT --recovery-delay=15000 --skip-if-exists
create region --name=BookOrder --type=PARTITION_REDUNDANT --skip-if-exists
create region --name=InventoryItem --type=REPLICATE --skip-if-exists
