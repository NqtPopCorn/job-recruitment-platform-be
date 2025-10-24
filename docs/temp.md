```ts
interface ApiDataResponse<T> {
  statusCode: number,
  message: string,
  data: T
}

interface ApiPageResponse<T> {
  statusCode: 200,
  message: string,
  results: T[],
  meta: {
    totalItems: number,
    currentPage: number,
    pageSize:  number,
    totalPages: number
  }
}
```

# Candidate

## 1. GET Page request

> GET /api/v1/candidate?page=1&size=10 (@RequestBody CandidateRequest, Pageable pageable)

```ts
interface CandidateRequest {
  search: string
  location: string
  industry: string
  experience: number
  education: string
  gender: `male` | `female` | `other`
}

interface CandidateResponse {
  id: number,
  avatar: string,
  name: string
  designation: string,
  location: string,
  hourlyRate: number,
  tags: string[],
  category: string,
  gender: string,
  createdAt: string,
  status: boolean,
  socialMedias: SocialMediaResponse[]
}

interface SocialMediaResponse {
  platform: string,
  url: string
}

```

## 2. POST NEW CANDIDATE

> POST /api/v1/candidate (@RequestBody CreateCandidateDto)

```ts
interface CreateCandidateDto {
  userId        : number
  name          : string
  birthday      : string | null
  phone         : string
  industry      : string
  skills        : array
  avatar        : string
  designation   : string
  location      : string
  hourlyRate    : number | null
  description   : string
  experience    : string
  currentSalary : string
  expectedSalary: string
  gender        : string
  language      : array
  educationLevel: string
  socialMedias  :SocialMediaResponse[]
}

```

## 3. GET DETAIL CANDIDATE BY ID
> GET /api/v1/candidate/details/:id

