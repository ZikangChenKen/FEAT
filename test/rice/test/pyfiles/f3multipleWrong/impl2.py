def func3(set_val, list_val, tup_val):
    if (set_val):
        return tuple([str(min(set_val) + 7)])
    elif (len(list_val) > len(tup_val)):
        return tuple([str(list_val[0] - 7)])
    elif (tup_val):
        return tuple([str(tup_val[0] + 7)])
    return ('0',)
