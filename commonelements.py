List1=[1,2,3,5,7]
List2=[2,3,7,8]
output = []
for i in range(len(List1)):
    for j in range(len(List2)):
        if List1[i]==List2[j]:
            output.append(List1[i])
print(output)


