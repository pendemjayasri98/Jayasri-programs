L = [1, 2, 2, 2, 4, 3, 4]
duplicate = []
d={}
for i in range(0, len(L)):
    for j in range(i + 1, len(L)):
        if L[j] == L[i]:
            duplicate.append(L[i])


print(duplicate)

L = [1, 2, 2, 2, 4, 3, 4]
d=[]
for i in L:
    if i not in d:
            d.append(i)
print(d)

