def func0(intval):
    return intval

import sys

if __name__ == "__main__":
    args = sys.argv[1:]
    new_args = [eval(arg) for arg in args]
    print (repr(func0(*new_args)))