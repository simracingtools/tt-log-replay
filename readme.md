# Replay a logfile to tt-cloud-server

    Usage:
    java -jar tt-log-replay-<version>.jar [--timed] [--url=<URL>] [--stop] <logfile-path>
    
    Options:
      --timed : Use logfile timestamps on log replay (replay in real time frame)
      --url   : tt-cloud-server URL the log messages are send to
      --stop  : Stop replay if an error occurred while sending a message
