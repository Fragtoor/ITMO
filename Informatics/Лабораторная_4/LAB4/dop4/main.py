# Author = Sivkov Alexander Vladimirovich
# Group = P3116
# Date = 06.11.2025
# Variant 11

import sys
import os

sys.path.append(os.path.join(os.path.dirname(__file__), '..'))

import timeit
from dop1.main import run_task_1
from dop2.main import run_task_2


project_root = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
sys.path.insert(0, project_root)


def lib_converter():
    yaml_filename = os.path.join(project_root, "docs", "in.yaml")
    hcl_filename = os.path.join(project_root, "docs", "out.hcl")
    run_task_1(yaml_filename, hcl_filename)


def my_converter():
    yaml_filename = os.path.join(project_root, "docs", "in.yaml")
    hcl_filename = os.path.join(project_root, "docs", "out2.hcl")
    run_task_2(yaml_filename, hcl_filename)


if __name__ == "__main__":
    time_my = timeit.timeit(my_converter, number=100)
    time_lib = timeit.timeit(lib_converter, number=100)

    print(f"{time_my} seconds - my_converter")
    print(f"{time_lib} seconds - lib_converter")
