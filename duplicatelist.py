L = [1, 2, 2, 2, 4, 3, 4]
# duplicate = []
dic={}
for i in range(0, len(L)):
    value = L[i]
    for j in range(i + 1, len(L)):
        if L[j] == value:
            dic[value] = value

print(dic.keys())

#-------------------------------------

L = [1, 2, 2, 2, 4, 3, 4]
d=[]
for i in L:
    if i not in d:
            d.append(i)
print(d)

