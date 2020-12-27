cmake_minimum_required(VERSION 3.5)

project(p1 LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

add_executable(p1 main.cpp problem03.cpp problem03.h problem04.h parseutils.cpp)

