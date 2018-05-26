import json

def get_max(arr, start, stop):
    m = 0
    mi = -1
    for i in range(start, stop):
        if arr[i] > m:
            m = arr[i]
            mi = i
    return mi - start

with open("data/training_set_y.json", "r") as ein:
    expected = json.load(ein)['array']

with open("results.json", "r") as pin:
    predicted = json.load(pin)['results']

print(len(expected))
print(len(predicted))



count = 0
total_count = 0

for i in range(len(expected)):
    e = expected[i]
    p = predicted[i]

    print(e[121:126])
    print(p[121:126])

    eh = get_max(e, 0, 121)
    es = get_max(e, 121, 126)

    ph = get_max(p, 0, 121)
    ps = get_max(p, 121, 126)

    same = eh == ph and es == ps

    if same:
        count += 1
    total_count += 1

    print(str(eh) + " " + str(es))
    print(str(ph) + " " + str(ps))
    print("===========================================")

print(str(count) + " of " + str(total_count))