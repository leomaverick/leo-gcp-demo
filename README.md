# leo-gcp-demo



# Dispatch message
PROJECT_ID=leo-main-project-471713
REGION=us-west1
FUNC=order-orchestrator
TOPIC=order-created


gcloud pubsub topics publish "$TOPIC" --project="$PROJECT_ID" --message='{"ping":"ok"}'


gcloud functions logs read "$FUNC" --gen2 --region="$REGION" --project="$PROJECT_ID" --limit=100