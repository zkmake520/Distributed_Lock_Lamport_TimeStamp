# Ricart Agrawala distributed lock in emulated distributed system
##System Model
  1. N processes (servers): emulated by N threads
  2. Messages are eventually delivered: used reliable TCP protocol
  
##TODO
  1.Failures can occur during message transition
  2.Servers can fail
