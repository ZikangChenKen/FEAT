def func3(set_val, list_val, tup_val):
    if (set_val):
        return tuple([str(min(set_val)), str(min(set_val) + 1)])
    elif (len(list_val) > len(tup_val)):
        return tuple([str(list_val[0]), str(list_val[0] + 1)])
    elif (tup_val):
        return tuple([str(tup_val[0]), str(tup_val[0] + 1)])
    return tuple(['0', '1'])

import sys

if __name__ == "__main__":
    args = sys.argv[1:]
    new_args = [eval(arg) for arg in args]
    print (repr(func3(*new_args)))