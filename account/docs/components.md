```mermaid
flowchart TB
    subgraph AccountService
        Controllers
        UseCases
        Domain
        Persistence
    end

    Controllers --> UseCases
    UseCases --> Domain
    UseCases --> Persistence
    Persistence --> PostgreSQL
```
