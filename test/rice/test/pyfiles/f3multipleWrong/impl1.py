def func3(set_val, list_val, tup_val):
    if (set_val):
        return tuple([str(min(set_val) - 1)])
    elif (len(list_val) > len(tup_val)):
        return tuple([str(list_val[0] - 2)])
    elif (tup_val):
        return tuple([str(tup_val[0] - 3)])
    return ('0',)
