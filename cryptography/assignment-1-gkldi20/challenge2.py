

inp1 = input().strip()
inp2 = input().strip()

inp1 = int(inp1,16)
inp2 = int(inp2,16)
res = inp1 ^ inp2

print("{:x}".format(res))
