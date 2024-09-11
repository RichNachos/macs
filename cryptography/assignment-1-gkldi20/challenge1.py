# add your imports here
import base64

# reading input (don't forget strip in other challenges!)
str_hex = input().strip()

str_base64 = base64.b64encode(bytes.fromhex(str_hex)).decode()

print(str_base64)
