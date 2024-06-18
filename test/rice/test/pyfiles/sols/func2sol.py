def func2(dict_val):
    retval = []
    for key, val in dict_val.items():
        retval.append(key + str(val))

    retval.sort()
    return retval
