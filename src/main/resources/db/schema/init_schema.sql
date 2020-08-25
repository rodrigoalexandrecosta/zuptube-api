CREATE USER bootcamp WITH password 'bootcamp';

CREATE SCHEMA zuptubeapi_service;
ALTER USER bootcamp SET search_path = 'zuptubeapi_service, public';

GRANT USAGE, CREATE ON SCHEMA zuptubeapi_service TO bootcamp;
GRANT ALL ON ALL TABLES IN SCHEMA zuptubeapi_service TO bootcamp;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA zuptubeapi_service TO bootcamp;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA zuptubeapi_service TO bootcamp;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA zuptubeapi_service TO bootcamp;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
GRANT EXECUTE ON FUNCTION public.uuid_generate_v4() TO bootcamp;