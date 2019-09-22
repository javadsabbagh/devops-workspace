class Person {
    String name
    int age
}

Person p = new Person(name: "Tome", age: 24)

println p.name + ' ' + p.age

def dir = new File('/run/media/javad/2E48FBFC48FBC11F/DATA-Partition/Video Coursers/DevOps/CI-CD/Packtpub Effective Jenkins Continuous Delivery with Jenkins Pipeline/')

dir.each {
    println it
}
println dir.isDirectory()
