# Makefile

# Variables
COMPOSEYML = -f ./docker-compose.yml
COMPOSE = docker-compose $(COMPOSEYML)

# Default target
.PHONY: up
up:
	@echo "Starting up containers..."
	$(COMPOSE) up --build -d

.PHONY: down
down:
	@echo "Stopping and removing containers..."
	$(COMPOSE) down

.PHONY: logs
logs:
	@echo "Displaying logs..."
	$(COMPOSE) logs -f

.PHONY: ps
ps:
	@echo "Listing containers..."
	$(COMPOSE) ps

.PHONY: restart
restart:
	@echo "Restarting containers..."
	$(COMPOSE) restart

.PHONY: clean
clean:
	@echo "Removing containers and networks..."
	$(COMPOSE) down -v

.PHONY: build
build:
	@echo "Building containers..."
	$(COMPOSE) build

.PHONY: test # Run backend tests
test:
	@echo "Running backend tests..."
	$(COMPOSE) exec backend ./gradlew test

.PHONY: help
help:
	@echo "Usage: make [target]"
	@echo ""
	@echo "Targets:"
	@echo "  up        Start containers in detached mode with build"
	@echo "  down      Stop and remove containers"
	@echo "  logs      Display logs"
	@echo "  ps        List containers"
	@echo "  restart   Restart containers"
	@echo "  clean     Remove containers and networks"
	@echo "  build     Build containers"
	@echo "  help      Display this help message"
