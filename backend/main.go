package main

import (
	"log"
)

func main() {
	db, err := NewPostgresDB()
	if err != nil {
		log.Fatal(err)
	}
	if err := db.Init(); err != nil {
		log.Fatal(err)
	}
	server := NewWebAPIServer(":3000", db)
	server.Run()
}
