import sys
from importlib import import_module
from expected import results

def test_buggy_impl(case_num, impl_name, fname, args):
    mod_name = impl_name[:-3]
    mod = import_module(mod_name)
    func = getattr(mod, fname)
    actual = func(*args)
    expected = results[case_num]
    return (actual == expected)

if __name__ == "__main__":
    case_num = int(sys.argv[1])
    impl_name = sys.argv[2]
    fname = sys.argv[3]
    args = sys.argv[4:]
    args = [eval(arg) for arg in args]
    print (test_buggy_impl(case_num, impl_name, fname, args))