# advent-of-code-2020

## Conclusions

The following conclusions are in the scope of the competition which favors speed coding over other aspects. 

 * Functional approach wins in speed and correctness. So many little bugs due to explicit iterations and more code to write to create new collections
 * `C++` has poor data massaging due to the purpose of using as little memory as possible. This means more code is needed to do the data massage
 * `Scala` Some little quirks makes it less suitable for speed coding
   * The slow toolchain and IDE quirks (eating cpu 100%)
   * the weird debugger jumps
   * no breakable / returning from within various scopes
 * `Kotlin` 
   * lacking the deep copy similar to C++ when it comes to mutable collections so its not easy to work with the mutable collections
   * generally fast compiler and code analysis in IDE
   * weird compiler bugs when data classes were nested with function
   * very easy type extension, loved it
   * powerfull but useless iteration breaking (e.g returning from within lambda functions or breaking two levels of loops)
   * breakab
 * `Rust` 
   * a steep learning curve, does not look fast to develop 
   * the binary packages are cool
   * if this is alternative to C++, not sure
 * `Python` Maybe good but the lack of type safety
 * `C` - maybe someday, the lack of any data structures makes things atrocious
 * `XLS` - can be a fun alternative :)
