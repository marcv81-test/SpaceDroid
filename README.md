Intro
=====

The game does not rely on any third-party framework because I wanted to learn OpenGL ES.

Build
=====

Dependencies (Ubuntu)
---------------------

    sudo apt-get install lib32stdc++6 lib32z1

Buck
----

    git clone https://github.com/facebook/buck.git
    cd buck
    ant
    sudo ln -s ${PWD}/bin/buck /usr/bin/buck
    sudo ln -s ${PWD}/bin/buckd /usr/bin/buckd

Watchman
--------

    git clone https://github.com/facebook/watchman.git
    cd watchman
    ./autogen.sh
    ./configure
    make
    sudo make install
